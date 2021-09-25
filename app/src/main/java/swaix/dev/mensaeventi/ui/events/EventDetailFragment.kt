package swaix.dev.mensaeventi.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.adapters.EventActivityAdapter
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.FragmentEventDetailBinding
import swaix.dev.mensaeventi.model.ResponseGetEventActivities
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

            eventName.text = args.eventDetail.name
            eventYear.text = args.eventDetail.dateFrom.yearString()
            eventDescription.text = args.eventDetail.description

            eventActivityList.adapter = EventActivityAdapter {
                Toast.makeText(requireContext(), "Cliccato ${it.name} - GESTIRE REMINDER?", Toast.LENGTH_LONG).show()
            }

            viewModel.fetchEventDetails(args.eventDetail.id.toString())
            viewModel.eventsActivities.observe(viewLifecycleOwner, object : NetworkObserver<ResponseGetEventActivities>() {
                override fun onSuccess(value: ResponseGetEventActivities) {
                    (eventActivityList.adapter as EventActivityAdapter).updateDataset(value.eventActivity)
                }
            })
        }
    }

    override fun isBottomBarVisible(): Boolean = false


}