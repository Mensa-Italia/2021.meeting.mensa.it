package swaix.dev.mensaeventi.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import swaix.dev.mensaeventi.databinding.EventDetailsExtraFragmentBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.asHtml
import swaix.dev.mensaeventi.utils.dayString
import swaix.dev.mensaeventi.utils.hourMinuteString
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
            description.text = args.extra.description.asHtml()
            if (args.extra is EventItemWithDate) {
                with((args.extra as EventItemWithDate)) {
                    day.text = dateFrom.dayString()
                    to.text = dateTo.hourMinuteString()
                    from.text = dateFrom.hourMinuteString()
                }
            }

            eventDetailExtraContactList.adapter = EventContactAdapter {
                setContactClickListener(it)
            }.apply { updateContacts(args.extra) }

            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }
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