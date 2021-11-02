package swaix.dev.mensaeventi.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.EventContactAdapter
import swaix.dev.mensaeventi.databinding.EventDetailsExtraFragmentBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.*


@AndroidEntryPoint
class EventDetailExtraFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private val args: EventDetailExtraFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return EventDetailsExtraFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment?)?.apply {
            getMapAsync(this@EventDetailExtraFragment)
        }

        with(EventDetailsExtraFragmentBinding.bind(view)) {
            name.text = args.extra.name.asHtml()
            activityDescription.text = args.extra.description.asHtml()
            if (args.extra is EventItemWithDate) {
                with((args.extra as EventItemWithDate)) {
                    activityDay.visibility = if (dateFrom.dayString().isBlank()) View.GONE else View.VISIBLE
                    activityTime.visibility = if (dateFrom.hourMinuteString().isBlank()) View.GONE else View.VISIBLE
                    activityDay.text = dateFrom.dayString()
                    val dateHourMinute = dateFrom.hourMinuteString() + " - " + dateTo.hourMinuteString()
                    activityTime.text = dateHourMinute
                }
            } else {
                activityDay.visibility = View.GONE
                activityTime.visibility = View.GONE
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
        mMap = googleMap
        val position = LatLng(args.extra.position.latitude, args.extra.position.longitude)
        with(mMap) {
            animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13.0f), object : GoogleMap.CancelableCallback {
                override fun onCancel() {
                }

                override fun onFinish() {
                    addMarker(requireContext(), position, args.extra)
                }

            })
            uiSettings.setAllGesturesEnabled(false)
        }
    }


}