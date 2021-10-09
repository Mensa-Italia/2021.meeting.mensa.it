package swaix.dev.mensaeventi.ui.events

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.EventContactAdapter
import swaix.dev.mensaeventi.adapters.EventExtraAdapter
import swaix.dev.mensaeventi.adapters.CalendarFragmentAdapter
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.EventDetailFragmentBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.*


@AndroidEntryPoint
class EventDetailFragment : BaseFragment() {

    private val viewModel: EventsViewModel by viewModels()

    private val args: EventDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return EventDetailFragmentBinding.inflate(inflater, container, false).root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(EventDetailFragmentBinding.bind(view)) {


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
//                Toast.makeText(requireContext(), "Cliccato ${it.name} - TBD??", Toast.LENGTH_LONG).show()
                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(it))
            }
//            eventActivityList.adapter = EventActivityAdapter {
//                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(it))
//            }
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

//                    eventTest.data = value.eventActivities
//                    eventTest.startCalc()

                    calendarDaysPager.adapter = CalendarFragmentAdapter(this@EventDetailFragment, value )

//                    calendarDaysPager.adapter = EventCalendarAdapter(
//                        days,
//                        {
//                            findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(it))
//                        },
//                        {
//                            val gmmIntentUri = Uri.parse("google.navigation:q=${it.position.latitude},${it.position.longitude}")
//                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                            mapIntent.setPackage("com.google.android.apps.maps")
//                            startActivity(mapIntent)
//                        })


                    TabLayoutMediator(calendarDaysTabs, calendarDaysPager) { tab, position ->
                        tab.text = days.keys.toTypedArray()[position]
                    }.attach()

                    (eventSuggestionsList.adapter as EventExtraAdapter).updateDataset(value.eventsSuggestions)
                    (eventContactList.adapter as EventContactAdapter).updateContacts(value.eventsContacts)


                }

            })
        }
    }



    private fun checkForEmptyState(view: View, value: ResponseGetEventDetails){
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