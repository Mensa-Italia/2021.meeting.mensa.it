package swaix.dev.mensaeventi.ui.events

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.BuildConfig
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.CalendarFragmentAdapter
import swaix.dev.mensaeventi.adapters.EventContactAdapter
import swaix.dev.mensaeventi.adapters.EventExtraAdapter
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.EventDetailFragmentBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.*
import java.util.*


@AndroidEntryPoint
class EventDetailFragment : BaseFragment() {

    private val viewModel: EventsViewModel by viewModels()

    private val args: EventDetailFragmentArgs by navArgs()

    lateinit var binding: EventDetailFragmentBinding
    lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
    lateinit var cameraPermissionRequest: ActivityResultLauncher<Array<String>>


    companion object {
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )

        private const val STATE_LENS_FACING = "lens_facing"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.entries.filter { it.value == true }.size == LOCATION_PERMISSIONS.size){
                    if (binding.sharePosition.isChecked) {
                        LocationForegroundService.startLocationService(requireContext())
                    } else {
                        LocationForegroundService.stopLocationService(requireContext())
                    }
                }
                else
                    Toast.makeText(requireContext(), "DEVI DARE I PERMESSI MANUALMENTE", Toast.LENGTH_LONG).show()
            }

        cameraPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.entries.filter { it.value == true }.size == CAMERA_PERMISSIONS.size)
                    findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToBarcodeReaderFragment())
                else
                    Toast.makeText(requireContext(), "DEVI DARE I PERMESSI MANUALMENTE", Toast.LENGTH_LONG).show()
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = EventDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        with(EventDetailFragmentBinding.bind(view)) {

            baseViewModel.locationServiceEnable.observe(viewLifecycleOwner, {
                sharePosition.isChecked = it
            })

            eventDetailsToolbar.eventName.text = (args.item.name + " " + args.item.dateFrom.yearString()).asHtml()

            eventDetailsToolbar.backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            Glide.with(requireContext())
                .load(args.item.imageURL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(eventDetailsToolbar.background)


            eventDescription.text = args.item.description.asHtml()

            viewModel.fetEventDetails(args.item.id.toString())

            eventHotelsSearch.addListener(object : SearchBarLabel.OnEventListener {

                override fun onTextChanged(value: String) {
                    Log.d(TAG, "onTextChanged: $value")
                    (eventHotelsList.adapter as EventExtraAdapter).filter(value)
                }
            })

            eventSuggestionsSearch.addListener(object : SearchBarLabel.OnEventListener {

                override fun onTextChanged(value: String) {
                    Log.d(TAG, "onTextChanged: $value")
                    (eventSuggestionsList.adapter as EventExtraAdapter).filter(value)
                }
            })

            eventHotelsList.adapter = EventExtraAdapter {
                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(it))
            }
            eventSuggestionsList.adapter = EventExtraAdapter {
                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(it))
            }

            eventContactList.adapter = EventContactAdapter {
                setContactClickListener(it)
            }


            viewModel.eventDetails.observe(viewLifecycleOwner, object : NetworkObserver<ResponseGetEventDetails>() {
                override fun onSuccess(value: ResponseGetEventDetails) {
                    (eventHotelsList.adapter as EventExtraAdapter).updateDataset(value.eventHotel)

                    checkForEmptyState(view, value)

                    val days: Map<String, List<EventItemWithDate>> = value.eventActivities
                        .sortedBy {
                            it.dateFrom
                        }
                        .groupBy {
                            it.dateFrom.shortDayString()
                        }

                    calendarDaysPager.adapter = CalendarFragmentAdapter(this@EventDetailFragment, value)


                    TabLayoutMediator(calendarDaysTabs, calendarDaysPager) { tab, position ->
                        tab.text = days.keys.toTypedArray()[position]
                    }.attach()

                    (eventSuggestionsList.adapter as EventExtraAdapter).updateDataset(value.eventsSuggestions)
                    (eventContactList.adapter as EventContactAdapter).updateContacts(value.eventsContacts)



                    sharePosition.isEnabled = Date().before(value.dateFrom) && Date().after(value.dateTo) || BuildConfig.DEV

                    sharePosition.setOnClickListener {
                        permissionRequest.launch(LOCATION_PERMISSIONS)
                    }

                    checkIn.setOnClickListener {
                        cameraPermissionRequest.launch(CAMERA_PERMISSIONS)
                    }

                }

            })
        }
    }


    private fun checkForEmptyState(view: View, value: ResponseGetEventDetails) {
        var counter = 0
        with(EventDetailFragmentBinding.bind(view)) {
            if (value.description.isEmpty()) counter++
            descriptionLabel.visibility = if (value.description.isEmpty()) View.GONE else View.VISIBLE
            eventDescription.visibility = if (value.description.isEmpty()) View.GONE else View.VISIBLE

            if (value.eventActivities.isNullOrEmpty()) counter++
            calendarDaysTabs.visibility = if (value.eventActivities.isNullOrEmpty()) View.GONE else View.VISIBLE
            calendarDaysPager.visibility = if (value.eventActivities.isNullOrEmpty()) View.GONE else View.VISIBLE

            if (value.eventHotel.isNullOrEmpty()) counter++
            eventHotels.visibility = if (value.eventHotel.isNullOrEmpty()) View.GONE else View.VISIBLE
            eventHotelsList.visibility = if (value.eventHotel.isNullOrEmpty()) View.GONE else View.VISIBLE
            eventHotelsSearch.visibility = if (value.eventHotel.isNullOrEmpty()) View.GONE else View.VISIBLE

            if (value.eventsSuggestions.isNullOrEmpty()) counter++
            eventSuggestions.visibility = if (value.eventsSuggestions.isNullOrEmpty()) View.GONE else View.VISIBLE
            eventSuggestionsList.visibility = if (value.eventsSuggestions.isNullOrEmpty()) View.GONE else View.VISIBLE
            eventSuggestionsSearch.visibility = if (value.eventsSuggestions.isNullOrEmpty()) View.GONE else View.VISIBLE

            if (value.eventsContacts.isNullOrEmpty()) counter++
            eventContactLabel.visibility = if (value.eventsContacts.isNullOrEmpty()) View.GONE else View.VISIBLE
            eventContactList.visibility = if (value.eventsContacts.isNullOrEmpty()) View.GONE else View.VISIBLE

            emptyStateMessage.visibility = if (counter == 5) View.VISIBLE else View.GONE
        }
    }

}