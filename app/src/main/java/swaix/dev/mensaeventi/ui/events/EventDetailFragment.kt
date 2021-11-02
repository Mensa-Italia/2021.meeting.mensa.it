package swaix.dev.mensaeventi.ui.events

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.CalendarFragmentAdapter
import swaix.dev.mensaeventi.adapters.EventContactAdapter
import swaix.dev.mensaeventi.adapters.EventExtraAdapter
import swaix.dev.mensaeventi.api.NetworkResult
import swaix.dev.mensaeventi.databinding.EventDetailFragmentBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.*
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class EventDetailFragment : BaseFragment() {

    private val viewModel: EventsViewModel by viewModels()

    private val args: EventDetailFragmentArgs by navArgs()

    lateinit var binding: EventDetailFragmentBinding
    lateinit var cameraPermissionRequest: ActivityResultLauncher<Array<String>>
    private var eventId: String = ""


    companion object {
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.entries.filter { it.value == true }.size == CAMERA_PERMISSIONS.size)
                    openBarcodeReader()
                else
                    Toast.makeText(requireContext(), "DEVI DARE I PERMESSI MANUALMENTE", Toast.LENGTH_LONG).show()
            }
    }

    private fun openBarcodeReader() {
        findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToBarcodeReaderFragment(args.item))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = EventDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        with(EventDetailFragmentBinding.bind(view)) {

            val details = args.item

            eventDetailsToolbar.eventName.text = (args.item.name + " " + args.item.dateFrom.yearString()).asHtml()

            eventDetailsToolbar.backArrow.setOnClickListener {
                findNavController().navigateUp()
            }

            Glide.with(requireContext())
                .load(args.item.imageURL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(eventDetailsToolbar.background)


            eventDescription.text = args.item.description.asHtml()

            eventHotelsSearch.addListener(object : SearchBarLabel.OnEventListener {

                override fun onTextChanged(value: String) {
                    Timber.d("onTextChanged: $value")
                    (eventHotelsList.adapter as EventExtraAdapter).filter(value)
                }
            })

            eventSuggestionsSearch.addListener(object : SearchBarLabel.OnEventListener {
                override fun onTextChanged(value: String) {
                    Timber.d("onTextChanged: $value")
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

            baseViewModel.hasCheckedIn.observe(viewLifecycleOwner, {
                Timber.d("**** HAS CHECKEDIN get VALUE: $it")
                if (!it.isNullOrEmpty() && requireContext().isLogged()) {
                    Timber.d("**** HAS CHECKEDIN && account created")
                    fetchIsUserCheckedIn()
                }
            })

            (eventHotelsList.adapter as EventExtraAdapter).updateDataset(details.eventHotel)
            checkForEmptyState(root, details)

            val days: Map<String, List<EventItemWithDate>> = details.eventActivities
                .sortedBy {
                    it.dateFrom
                }
                .groupBy {
                    it.dateFrom.shortDayString()
                }

            calendarDaysPager.adapter = CalendarFragmentAdapter(this@EventDetailFragment, details)

            TabLayoutMediator(calendarDaysTabs, calendarDaysPager) { tab, position ->
                tab.text = days.keys.toTypedArray()[position]
            }.attach()

            (eventSuggestionsList.adapter as EventExtraAdapter).updateDataset(details.eventsSuggestions)
            (eventContactList.adapter as EventContactAdapter).updateContacts(details.eventsContacts)

            showMap.visibility = View.GONE
            if (requireContext().isLogged())
                fetchIsUserCheckedIn()
            else
                checkIn.visibility = View.VISIBLE

            checkIn.setOnClickListener {
                if (requireContext().hasPermissions(*CAMERA_PERMISSIONS))
                    openBarcodeReader()
                else
                    cameraPermissionRequest.launch(CAMERA_PERMISSIONS)
            }

        }
    }


    private fun fetchIsUserCheckedIn() {
        with(binding) {
            lifecycleScope.launch {
                viewModel.isUserCheckedIn(requireContext().getAccountPassword(), args.item.id)
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { networkResult ->
                        when (networkResult) {
                            is NetworkResult.Success -> {
                                networkResult.data?.let { value ->
                                    eventId = value.eventIdQR
                                    TransitionManager.beginDelayedTransition(root)
                                    if (value.isCheckedIn && value.eventId == args.item.id) {
                                        showMap.visibility = View.VISIBLE
                                        checkIn.visibility = View.GONE

                                        showMap.setOnClickListener {
                                            findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToMapFragment(value.eventIdQR, requireContext().getAccountPassword(), args.item))
                                        }

                                    } else {
                                        checkIn.visibility = View.VISIBLE
                                        showMap.visibility = View.GONE
                                    }
                                }
                            }
                            is NetworkResult.Error -> {
                                checkIn.visibility = View.VISIBLE
                                showMap.visibility = View.GONE
                            }
                            else -> {
                            }
                        }
                    }
            }
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