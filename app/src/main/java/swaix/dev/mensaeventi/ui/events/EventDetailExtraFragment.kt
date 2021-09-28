package swaix.dev.mensaeventi.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.EventContactAdapter
import swaix.dev.mensaeventi.adapters.Item
import swaix.dev.mensaeventi.databinding.EventDetailsExtraFragmentBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.dateString
import swaix.dev.mensaeventi.utils.setContactClickListener

@AndroidEntryPoint
class EventDetailExtraFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val viewModel: EventsViewModel by viewModels()

    private val args: EventDetailExtraFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return EventDetailsExtraFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment?)?.apply {
            getMapAsync(this@EventDetailExtraFragment)
        }

        with(EventDetailsExtraFragmentBinding.bind(view)) {
            name.text = args.extra.name
            description.text = args.extra.description
            if (args.extra is EventItemWithDate) {
                to.text = (args.extra as EventItemWithDate).dateTo.dateString()
                from.text = (args.extra as EventItemWithDate).dateFrom.dateString()
            }

            eventDetailExtraContactList.adapter = EventContactAdapter {
                setContactClickListener(it)
            }.apply { updateContacts(args.extra) }

        }
    }




    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.fetchEventsResponse()
        mMap = googleMap
        with(args.extra.position) {
            val position = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(position).title(args.extra.name))
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(position))

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 12.0f))


        }
    }
}