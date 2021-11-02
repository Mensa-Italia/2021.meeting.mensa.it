package swaix.dev.mensaeventi.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import swaix.dev.mensaeventi.utils.LocationForegroundService

open class BaseFragment : Fragment() {

    val baseViewModel: BaseViewModel by activityViewModels()

}