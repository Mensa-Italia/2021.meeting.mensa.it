package swaix.dev.mensaeventi.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.api.LoadingManager
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.MapFragmentBinding
import swaix.dev.mensaeventi.model.ResponseGetUserPositions
import swaix.dev.mensaeventi.model.UserPosition
import timber.log.Timber


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, LoadingManager {

    private lateinit var map: GoogleMap

    private val args: MapFragmentArgs by navArgs()
    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MapFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment?)?.apply {
            getMapAsync(this@MapFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.switchUserPosition(args.eventId, args.mensaId)
    }

    override fun onPause() {
        super.onPause()
        viewModel.switchUserPosition(args.eventId, args.mensaId)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewModel.userPositions.observe(viewLifecycleOwner, object : NetworkObserver<ResponseGetUserPositions>(this) {
            override fun onSuccess(value: ResponseGetUserPositions) {
                setUpClusterer(value.positions)
            }
        })
        // Add a marker in Sydney and move the camera
    }

    override fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            Timber.d("onLoading: $isLoading")
        } else {
            Timber.d("onLoading: $isLoading")
        }
    }

    // Declare a variable for the cluster manager.
    private lateinit var clusterManager: ClusterManager<MyItem>

    private fun setUpClusterer(positions: List<UserPosition>) {
        // Position the map.

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = ClusterManager(context, map)

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraIdleListener(clusterManager)
//        map.setOnMarkerClickListener(clusterManager)

        addItems(positions)

    }
    var isFirstTime = true

    private fun addItems(positions: List<UserPosition>) {

        clusterManager.clearItems()
        if (positions.any()) {
            val builder = LatLngBounds.builder()
            positions.forEach {
                val item = MyItem(it.latitude, it.longitude, it.name + " " + it.surname, it.name)
                clusterManager.addItem(item)
                builder.include(LatLng(it.latitude, it.longitude))
            }
            if(isFirstTime) {
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
        private val snippet: String
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

    }


}
