package swaix.dev.mensaeventi.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

open class BaseFragment : Fragment() {

    val baseViewModel: BaseViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        baseViewModel.showBottomBar.postValue(isBottomBarVisible())
    }

   open fun isBottomBarVisible() = true
}