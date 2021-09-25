package swaix.dev.mensaeventi.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.adapters.EventAdapter
import swaix.dev.mensaeventi.api.LoadingManager
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.EventsFragmentBinding
import swaix.dev.mensaeventi.model.MensaEvent
import swaix.dev.mensaeventi.model.ResponseGetEvents
import swaix.dev.mensaeventi.ui.BaseFragment


@AndroidEntryPoint
class EventsFragment : BaseFragment(), LoadingManager {

    private val viewModel: EventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return EventsFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(EventsFragmentBinding.bind(view)) {
            viewModel.fetchEventsResponse()
            eventList.adapter = EventAdapter {
                findNavController().navigate(EventsFragmentDirections.actionNavigationHomeToEventDetailFragment(it))
            }
            viewModel.events.observe(viewLifecycleOwner, object : NetworkObserver<ResponseGetEvents>(this@EventsFragment, {
                (eventList.adapter as EventAdapter).updateDataset(listOf())
            }) {
                override fun onSuccess(value: ResponseGetEvents) {
                    (eventList.adapter as EventAdapter).updateDataset(value.events)
                }
            })
        }
    }

    override fun onLoading(isLoading: Boolean) {

    }
}