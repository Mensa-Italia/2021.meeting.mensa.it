package swaix.dev.mensaeventi.ui.checkIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.databinding.CheckInFragmentBinding

@AndroidEntryPoint
class CheckInFragment : DialogFragment() {

    private val viewModel: CheckInViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        isCancelable = false
        return CheckInFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(CheckInFragmentBinding.bind(view)) {
            dialogClose.setOnClickListener {
                dismiss()
            }

            dialogCheckingButton.setOnClickListener {
                dismiss()
            }

        }
    }

}