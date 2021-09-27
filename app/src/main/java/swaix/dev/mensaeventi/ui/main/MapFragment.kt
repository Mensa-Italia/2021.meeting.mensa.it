package swaix.dev.mensaeventi.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.api.LoadingManager
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.MapFragmentBinding
import swaix.dev.mensaeventi.model.ResponseGetEvents
import swaix.dev.mensaeventi.utils.TAG


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, LoadingManager {

    private lateinit var mMap: GoogleMap

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


    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.fetchEventsResponse()

        viewModel.response.observe(viewLifecycleOwner, object : NetworkObserver<ResponseGetEvents>(this) {

            override fun onSuccess(value: ResponseGetEvents) {
            }

        })

        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            Log.d(TAG, "onLoading: $isLoading")
        } else {
            Log.d(TAG, "onLoading: $isLoading")
        }
    }


}
