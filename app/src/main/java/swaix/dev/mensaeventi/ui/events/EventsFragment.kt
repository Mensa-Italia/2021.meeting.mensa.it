package swaix.dev.mensaeventi.ui.events

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.EventAdapter
import swaix.dev.mensaeventi.api.LoadingManager
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.EventsFragmentBinding
import swaix.dev.mensaeventi.model.Events


@AndroidEntryPoint
class EventsFragment : Fragment(), LoadingManager {

    private val viewModel: EventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return EventsFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(EventsFragmentBinding.bind(view)){
            viewModel.fetchEventsResponse()
            viewModel.events.observe(viewLifecycleOwner, object : NetworkObserver<Events>(this@EventsFragment) {
                override fun onSuccess(t: Events) {
                    eventList.adapter = EventAdapter(t)
                }
            })
        }
    }

    override fun onLoading(isLoading: Boolean) {


    }
}