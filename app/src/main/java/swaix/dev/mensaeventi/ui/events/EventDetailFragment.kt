package swaix.dev.mensaeventi.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.adapters.EventActivityAdapter
import swaix.dev.mensaeventi.adapters.EventExtraAdapter
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.FragmentEventDetailBinding
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.yearString

@AndroidEntryPoint
class EventDetailFragment : BaseFragment() {

    private val viewModel: EventsViewModel by viewModels()

    private val args: EventDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentEventDetailBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(FragmentEventDetailBinding.bind(view)) {

            eventDetailsToolbar.eventName.text = args.item.description
            eventDetailsToolbar.eventYear.text = args.item.dateFrom.yearString()
            eventDescription.text = args.item.description




            viewModel.fetEventDetails(args.item.id.toString())
            eventHotelsList.adapter = EventExtraAdapter {
//                Toast.makeText(requireContext(), "Cliccato ${it.name} - TBD??", Toast.LENGTH_LONG).show()
                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(it))
            }
            eventActivityList.adapter = EventActivityAdapter {
                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(it))
            }
            eventSuggestionsList.adapter = EventExtraAdapter {
                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(it))
            }
            viewModel.eventDetails.observe(viewLifecycleOwner, object : NetworkObserver<ResponseGetEventDetails>() {
                override fun onSuccess(value: ResponseGetEventDetails) {
                    (eventHotelsList.adapter as EventExtraAdapter).updateDataset(value.eventHotel)
                    (eventActivityList.adapter as EventActivityAdapter).updateDataset(value.eventActivities)
                    (eventSuggestionsList.adapter as EventExtraAdapter).updateDataset(value.eventsSuggestions)
                }
            })

//
//
//
//
//            viewModel.fetchEventDetails(args.eventDetail.id.toString())
//            viewModel.eventsActivities.observe(viewLifecycleOwner, object : NetworkObserver<ResponseGetEventActivities>() {
//                override fun onSuccess(value: ResponseGetEventActivities) {
//                }
//            })
//
//
//            viewModel.fetchEventSuggestions(args.eventDetail.id.toString())
//            viewModel.suggestions.observe(viewLifecycleOwner, object : NetworkObserver<Suggestions>() {
//                override fun onSuccess(value: Suggestions) {
//                }
//            })
        }
    }

    override fun isBottomBarVisible(): Boolean = false


}