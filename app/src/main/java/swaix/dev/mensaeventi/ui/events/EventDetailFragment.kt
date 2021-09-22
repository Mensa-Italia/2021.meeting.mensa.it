package swaix.dev.mensaeventi.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import swaix.dev.mensaeventi.databinding.FragmentEventDetailBinding
import swaix.dev.mensaeventi.ui.BaseFragment

class EventDetailFragment : BaseFragment() {


    private val args : EventDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentEventDetailBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(FragmentEventDetailBinding.bind(view)){
            description.text = args.eventDetail.description
        }
    }

    override fun isBottomBarVisible(): Boolean = false


}