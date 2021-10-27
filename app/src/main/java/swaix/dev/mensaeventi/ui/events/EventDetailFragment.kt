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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.CalendarFragmentAdapter
import swaix.dev.mensaeventi.adapters.EventContactAdapter
import swaix.dev.mensaeventi.adapters.EventExtraAdapter
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.EventDetailFragmentBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.model.ResponseIsUserCheckedIn
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.*
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class EventDetailFragment : BaseFragment() {

    private val viewModel: EventsViewModel by viewModels()

    private val args: EventDetailFragmentArgs by navArgs()

    lateinit var binding: EventDetailFragmentBinding
    lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
    lateinit var cameraPermissionRequest: ActivityResultLauncher<Array<String>>
    private var eventId: String = ""


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
                if (permissions.entries.filter { it.value == true }.size == LOCATION_PERMISSIONS.size) {
                    manageLocationService(eventId)
                } else
                    Toast.makeText(requireContext(), "DEVI DARE I PERMESSI MANUALMENTE", Toast.LENGTH_LONG).show()
            }

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

    private fun manageLocationService(eventId: String) {
        if (binding.sharePosition.isChecked && eventId.isNotEmpty()) {
            LocationForegroundService.startLocationService(requireContext(), eventId)
        } else {
            LocationForegroundService.stopLocationService(requireContext())
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

            viewModel.fetchEventDetails(args.item.id.toString())

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

                    baseViewModel.hasCheckedIn.observe(viewLifecycleOwner, {
                        if (!it.isNullOrEmpty()) {
                            if (requireContext().isLogged())
                                viewModel.fetchIsUserCheckedIn(requireContext().getAccountPassword())
                            baseViewModel.hasCheckedIn.postValue("")
                        }
                    })

                    sharePosition.visibility = View.GONE
                    if (requireContext().isLogged())
                        viewModel.fetchIsUserCheckedIn(requireContext().getAccountPassword())
                    viewModel.isUserCheckedIn.observe(viewLifecycleOwner, object : NetworkObserver<ResponseIsUserCheckedIn>() {
                        override fun onSuccess(value: ResponseIsUserCheckedIn) {
                            eventId = value.eventIdQR
                            TransitionManager.beginDelayedTransition(root)
                            sharePosition.visibility = if (value.isCheckedIn && value.eventId == args.item.id) View.VISIBLE else View.GONE
                            sharePosition.setOnClickListener {
                                if (requireContext().hasPermissions(*LOCATION_PERMISSIONS))
                                    manageLocationService(value.eventIdQR)
                                else
                                    permissionRequest.launch(LOCATION_PERMISSIONS)
                            }

                            descriptionLabel.setOnClickListener {
                                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToMapFragment(value.eventIdQR, requireContext().getAccountPassword()))
                            }
                        }
                    })



                    checkIn.setOnClickListener {
                        if (requireContext().hasPermissions(*CAMERA_PERMISSIONS))
                            openBarcodeReader()
                        else
                            cameraPermissionRequest.launch(CAMERA_PERMISSIONS)
                    }

                }

            })
        }
    }

/*
    private fun getRequiredPermissions(): Array<String?> {
        return try {
            val info: PackageInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(requireContext(), it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        ) {
            Timber.i("Permission granted: $permission")
            return true
        }
        Timber.i("Permission NOT granted: $permission")
        return false
    }*/


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