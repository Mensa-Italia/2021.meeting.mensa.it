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
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.api.LoadingManager
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.MapFragmentBinding
import swaix.dev.mensaeventi.model.ResponseGetUserPositions
import timber.log.Timber


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, LoadingManager {

    private lateinit var mMap: GoogleMap

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
        mMap = googleMap

        viewModel.userPositions.observe(viewLifecycleOwner, object : NetworkObserver<ResponseGetUserPositions>(this) {
            override fun onSuccess(value: ResponseGetUserPositions) {
                if (value.positions.any()) {
                    value.positions.forEach {
                        val marker = LatLng(it.latitude, it.longitude)
                        mMap.addMarker(MarkerOptions().position(marker).title(it.name + " " + it.surname))
                    }

                    val minItem = value.positions.find {
                        it.latitude == value.positions.minOf { min ->
                            min.latitude
                        }
                    }
                    val maxItem = value.positions.find {
                        it.latitude == value.positions.maxOf { max ->
                            max.latitude
                        }
                    }
                    if (maxItem != null && minItem != null) {
                        val bounds = LatLngBounds(LatLng(minItem.latitude, minItem.longitude), LatLng(maxItem.latitude, maxItem.longitude))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.center, 12f))
                    }
                }
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


}
