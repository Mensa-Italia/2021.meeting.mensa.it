package swaix.dev.mensaeventi.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.adapters.EventAdapter
import swaix.dev.mensaeventi.api.LoadingManager
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.EventsFragmentBinding
import swaix.dev.mensaeventi.model.Events
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
            viewModel.events.observe(viewLifecycleOwner, object : NetworkObserver<Events>(this@EventsFragment) {
                override fun onSuccess(t: Events) {
                    (eventList.adapter as EventAdapter).updateDataset(t.items)
                }
            })
        }
    }

    override fun onLoading(isLoading: Boolean) {

    }
}