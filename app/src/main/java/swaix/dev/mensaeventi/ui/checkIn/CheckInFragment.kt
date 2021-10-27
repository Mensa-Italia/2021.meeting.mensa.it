package swaix.dev.mensaeventi.ui.checkIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.api.NetworkObserver
import swaix.dev.mensaeventi.databinding.CheckInFragmentBinding
import swaix.dev.mensaeventi.model.AckResponse
import swaix.dev.mensaeventi.ui.BaseViewModel
import swaix.dev.mensaeventi.utils.*

@AndroidEntryPoint
class CheckInFragment : DialogFragment() {

    private val viewModel: CheckInViewModel by viewModels()
    private val baseViewModel: BaseViewModel by activityViewModels()
    private val args: CheckInFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        isCancelable = false
        return CheckInFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(CheckInFragmentBinding.bind(view)) {
            if (requireContext().isLogged()) {
                dialogCheckInName.editText?.setText(requireContext().getAccountData(NAME))
                dialogCheckInSurname.editText?.setText(requireContext().getAccountData(SURNAME))
                dialogCheckInMensaId.editText?.setText(requireContext().getAccountData(MENSA_ID))
            }

            dialogClose.setOnClickListener {
                dismiss()
            }

            dialogCheckingButton.setOnClickListener {
                val name = dialogCheckInName.editText?.text.toString()
                val surname = dialogCheckInSurname.editText?.text.toString()
                val mensaId = dialogCheckInMensaId.editText?.text.toString()
                requireContext().createAccount(name, surname, mensaId)

                viewModel.saveUser(name, surname, args.eventId, mensaId)
            }

            viewModel.putUserResp.observe(viewLifecycleOwner, object : NetworkObserver<AckResponse>() {
                override fun onSuccess(value: AckResponse) {
                    baseViewModel.hasCheckedIn.postValue(args.eventId)
                    dismiss()
                }
            })
        }
    }
}