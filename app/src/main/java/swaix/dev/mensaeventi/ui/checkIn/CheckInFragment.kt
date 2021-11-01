package swaix.dev.mensaeventi.ui.checkIn

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.CheckInFragmentBinding
import swaix.dev.mensaeventi.ui.BaseViewModel
import swaix.dev.mensaeventi.utils.*
import timber.log.Timber

@AndroidEntryPoint
class CheckInFragment : DialogFragment() {

    private val viewModel: CheckInViewModel by viewModels()
    private val baseViewModel: BaseViewModel by activityViewModels()
    private val args: CheckInFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
                if (name.isEmpty()) {
                    dialogCheckInName.editText?.error = resources.getString(R.string.mandatory_label)
                }
                if (surname.isEmpty()) {
                    dialogCheckInSurname.editText?.error = resources.getString(R.string.mandatory_label)
                }
                if (mensaId.isEmpty()) {
                    dialogCheckInMensaId.editText?.error = resources.getString(R.string.mandatory_label)
                }
                if (name.isNotEmpty() && surname.isNotEmpty() && mensaId.isNotEmpty()) {
                    requireContext().createAccount(name, surname, mensaId)
                    lifecycleScope.launch {
                        viewModel.putUser(name, surname, args.eventId, mensaId)
                            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                            .collect { networkResult ->
                                networkResult.manage(onSuccess = {
                                    Timber.d("**** HAS CHECKEDIN POST VALUE")
                                    baseViewModel.hasCheckedIn_.postValue(args.eventId)
                                    dismiss()
                                }, onError = {
                                    Toast.makeText(requireContext(), "Errore durante il recupero dei dati, riprovare", Toast.LENGTH_LONG).show()
                                    findNavController().navigateUp()
                                })
                            }
                    }
                }
            }
        }
    }
}