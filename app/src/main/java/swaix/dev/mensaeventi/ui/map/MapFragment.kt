package swaix.dev.mensaeventi.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.databinding.MapFragmentBinding
import swaix.dev.mensaeventi.model.UserPosition
import swaix.dev.mensaeventi.ui.BaseFragment
import java.util.*


@AndroidEntryPoint
class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private val args: MapFragmentArgs by navArgs()
    private val viewModel: MapViewModel by viewModels()
    lateinit var binding : MapFragmentBinding

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


    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        lifecycleScope.launch {
            viewModel.getUsersPositions(args.eventId, args.mensaId)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (it is NetworkResult.Success) {
                        setUpClusterer(it.data?.positions ?: listOf())
                    }
                }
        }


        // Add a marker in Sydney and move the camera
    }

    override fun onResume() {
        super.onResume()

        baseViewModel.locationServiceEnable.observe(viewLifecycleOwner, {
            if(!it){
                Toast.makeText(requireContext(), R.string.location_service_off, Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        })

    }

    private lateinit var clusterManager: ClusterManager<MyItem>

    private fun setUpClusterer(positions: List<UserPosition>) {
        clusterManager = ClusterManager(context, map)
        clusterManager.renderer = context?.let { CustomClusterRenderer(it, map, clusterManager) }
        map.setOnCameraIdleListener(clusterManager)
        addItems(positions)
        binding.numberOfPeopleSharing.text = resources.getQuantityString(R.plurals.label_number_of_people_sharing, positions.size, positions.size)
    }

    var isFirstTime = true

    private fun addItems(positions: List<UserPosition>) {

        map.clear()
        clusterManager.clearItems()

        if (positions.any()) {
            val builder = LatLngBounds.builder()
            positions.forEach {
                val item = MyItem(it.latitude, it.longitude, it.name + " " + it.surname, it.name, it.name.substring(0,1).uppercase(Locale.getDefault())+" "+it.surname.substring(0,1).uppercase(Locale.getDefault()))
                clusterManager.addItem(item)
                builder.include(LatLng(it.latitude, it.longitude))
            }
            if (isFirstTime) {
                val bounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                map.moveCamera(cu)
                map.animateCamera(CameraUpdateFactory.zoomTo(13f), 2000, null)
                isFirstTime = false
            }
        }
        clusterManager.cluster()
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
