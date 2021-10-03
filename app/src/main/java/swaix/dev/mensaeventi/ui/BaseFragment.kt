package swaix.dev.mensaeventi.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

open class BaseFragment : Fragment() {

    private val baseViewModel: BaseViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
    }


}