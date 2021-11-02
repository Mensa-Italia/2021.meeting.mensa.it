package swaix.dev.mensaeventi.ui.map

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.MapFragmentBinding
import swaix.dev.mensaeventi.model.UserPosition
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.*
import java.util.*


@AndroidEntryPoint
class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private val args: MapFragmentArgs by navArgs()
    private val viewModel: MapViewModel by viewModels()
    private lateinit var binding: MapFragmentBinding
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>

    companion object {
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.entries.filter { it.value == true }.size == LOCATION_PERMISSIONS.size) {
                    manageLocationService()
                } else
                    Toast.makeText(requireContext(), "DEVI DARE I PERMESSI MANUALMENTE", Toast.LENGTH_LONG).show()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment?)?.apply {
            getMapAsync(this@MapFragment)
        }

        with(binding) {
            baseViewModel.locationServiceEnable.observe(viewLifecycleOwner, {
                sharingPeopleBox.isEnabled = true
                sharingPeopleBox.isSelected = it
                binding.sharingPeopleBox.text = getString(if (it) R.string.label_shared_position else R.string.label_share_position)
            })

            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            mapToolbarTitle.text = (args.details.name + " " + args.details.dateFrom.yearString()).asHtml()

            sharingPeopleBox.setOnClickListener {
                sharingPeopleBox.isEnabled = false
                if (requireContext().hasPermissions(*LOCATION_PERMISSIONS))
                    manageLocationService()
                else
                    permissionRequest.launch(LOCATION_PERMISSIONS)
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        clusterManager = ClusterManager(context, map)
        clusterManager.renderer = context?.let { CustomClusterRenderer(it, map, clusterManager) }
        map.setOnCameraIdleListener(clusterManager)

        lifecycleScope.launch {
            viewModel.getUsersPositions(args.eventId, args.mensaId)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { networkResult ->
                    networkResult.manage(onSuccess = {
                        addSharingPeople(it.positions)
                        binding.sharePositionPeople.text = resources.getQuantityString(R.plurals.label_number_of_people_sharing, it.positions.size, it.positions.size)
                    }, onError = {
                        Toast.makeText(requireContext(), "Errore durante il recupero dei dati, riprovare", Toast.LENGTH_LONG).show()
                        findNavController().navigateUp()
                    })
                }
        }

        addExtrasMarkers()


    }

    private lateinit var clusterManager: ClusterManager<MyItem>

    private fun manageLocationService() {
        val eventId = args.eventId
        if (baseViewModel.locationServiceEnable.value == false && eventId.isNotEmpty()) {
            LocationForegroundService.startLocationService(requireContext(), eventId)
        } else {
            clusterManager.clearItems()
            addExtrasMarkers()
            LocationForegroundService.stopLocationService(requireContext())
        }
    }

    var first = true

    private fun addExtrasMarkers() {

        val builder = LatLngBounds.builder()
        args.details.eventActivities.map {
            val position = LatLng(it.position.latitude, it.position.longitude)
            map.addMarker(requireContext(), position, it)
            builder.include(position)
        }
        args.details.eventHotel.map {
            val position = LatLng(it.position.latitude, it.position.longitude)
            map.addMarker(requireContext(), position, it)
            builder.include(position)
        }
        args.details.eventsSuggestions.map {
            val position = LatLng(it.position.latitude, it.position.longitude)
            map.addMarker(requireContext(), position, it)
            builder.include(position)
        }
        if (first) {
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
            map.moveCamera(cu)
            map.animateCamera(CameraUpdateFactory.zoomTo(12f), 2000, null)
            first = false
        }
    }

    private fun addSharingPeople(positions: List<UserPosition>) {
        clusterManager.clearItems()
        if (binding.sharingPeopleBox.isSelected) {
            positions.forEach {
                val item = MyItem(it.latitude, it.longitude, it.name + " " + it.surname, it.name, it.name.first().uppercase(Locale.getDefault()) + " " + it.surname.first().uppercase(Locale.getDefault()))
                clusterManager.addItem(item)
            }
        }
        clusterManager.cluster()
        addExtrasMarkers()
    }


    inner class MyItem(
        lat: Double,
        lng: Double,
        private val title: String,
        private val snippet: String,
        private val initials: String
    ) : ClusterItem {

        private val position: LatLng = LatLng(lat, lng)

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String {
            return title
        }

        override fun getSnippet(): String {
            return snippet
        }

        fun getInitials(): String {
            return initials
        }


    }


}
