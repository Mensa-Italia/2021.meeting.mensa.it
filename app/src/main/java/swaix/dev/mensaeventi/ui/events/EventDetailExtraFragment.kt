package swaix.dev.mensaeventi.ui.events

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.EventContactAdapter
import swaix.dev.mensaeventi.databinding.EventDetailsExtraFragmentBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.model.ItemType
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.asHtml
import swaix.dev.mensaeventi.utils.dayString
import swaix.dev.mensaeventi.utils.hourMinuteString
import swaix.dev.mensaeventi.utils.setContactClickListener


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
                    activityTime.text = dateFrom.hourMinuteString() + " - " + dateTo.hourMinuteString()
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
        with(args.extra.position) {
            val position = LatLng(latitude, longitude)
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1200)
                mMap.addMarker(MarkerOptions().position(position).title(args.extra.name))?.apply {

                    showInfoWindow()
                }?.setIcon(
                    requireContext().bitmapFromVector(
                        // TODO cambiare icone
                        when (args.extra.type) {
                            ItemType.HOTEL -> R.drawable.ic_hotel
                            ItemType.RESTAURANT -> R.drawable.ic_restaurant
                            ItemType.ACTIVITY -> R.drawable.ic_event_black_24dp
                            ItemType.EVENT -> R.drawable.ic_close
                            ItemType.CONTACT -> R.drawable.ic_close
                            ItemType.NONE -> R.drawable.ic_close
                        }
                    )
                )
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 17.0f))
            mMap.uiSettings.setAllGesturesEnabled(false)
        }
    }

    private fun Context.bitmapFromVector(vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}