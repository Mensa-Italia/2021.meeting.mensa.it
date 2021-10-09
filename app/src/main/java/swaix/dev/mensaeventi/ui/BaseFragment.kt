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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        checkIfEnabled()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkIfEnabled() {
        val connection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(
                className: ComponentName,
                service: IBinder
            ) {
                val binder: LocationForegroundService.LocationForegroundServiceBinder = service as LocationForegroundService.LocationForegroundServiceBinder
                val myService = binder.service
                baseViewModel.locationServiceEnable.postValue(myService.isRunning)
            }

            override fun onServiceDisconnected(arg0: ComponentName) {}
        }

        // Bind to MyService
        val intent = Intent(requireActivity(), LocationForegroundService::class.java)
        requireActivity().bindService(intent, connection, AppCompatActivity.BIND_AUTO_CREATE)
    }

}