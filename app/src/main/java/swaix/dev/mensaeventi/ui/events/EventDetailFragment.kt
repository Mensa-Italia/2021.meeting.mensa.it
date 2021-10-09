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
import androidx.lifecycle.MutableLiveData
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


    lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
    private var enableShareLocation = MutableLiveData(State.TO_BE_ASKED)

    companion object {
        val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationPermissionsArray = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val state = if (permissions.entries.filter { it.value == true }.size == locationPermissionsArray.size)
                    State.GRANTED
                else
                    State.DENIED
                enableShareLocation.postValue(state)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return EventDetailFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ???")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ????")
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

                    enableShareLocation.observe(viewLifecycleOwner) {
                        it?.let { state ->
                            when (state) {
                                State.GRANTED ->
                                    if (sharePosition.isChecked) {
                                        LocationForegroundService.startLocationService(requireContext())
                                    } else {
                                        LocationForegroundService.stopLocationService(requireContext())
                                    }
                                State.DENIED ->
                                    Toast.makeText(requireContext(), "DEVI DARE I PMERMESSI STRUNZ", Toast.LENGTH_LONG).show()
                                State.TO_BE_ASKED -> {

                                }
                            }
                        }

                    }

                    sharePosition.isEnabled = Date().before(value.dateFrom) && Date().after(value.dateTo) || BuildConfig.MOCK_DATA

                    sharePosition.setOnClickListener { _ ->
                        permissionRequest.launch(LOCATION_PERMISSIONS)
                    }

                }

            })
        }
    }


}