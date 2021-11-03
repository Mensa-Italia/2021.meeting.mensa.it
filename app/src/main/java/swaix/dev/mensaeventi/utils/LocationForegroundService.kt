package swaix.dev.mensaeventi.utils

import android.annotation.SuppressLint
import android.app.*
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.repository.DataRepository
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LocationForegroundService : Service() {
    companion object {
        private const val CHANNEL_ID = "LocationForegroundServiceChannel"

        private const val FOREGROUND_NOTE_ID = 2
        const val NEW_LOCATION = "new_location"
        const val STOP_SERVICE = "stop_service"
        const val START_SERVICE = "start_service"


        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 30000
        const val MAX_WAIT_TIME: Long = UPDATE_INTERVAL_IN_MILLISECONDS * 2
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        fun startLocationService(context: Context, eventId: String) {
            val intent = Intent(context, LocationForegroundService::class.java)
            intent.action = START_SERVICE
            intent.putExtras(bundleOf(EVENT_ID to eventId))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopLocationService(context: Context) {
            val intent = Intent(context, LocationForegroundService::class.java)
            intent.action = STOP_SERVICE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }


    @Inject
    lateinit var repository: DataRepository

    var eventQRid: String? = null
    private val mBinder: IBinder = LocationForegroundServiceBinder()
    private var mLocation: Location? = null
    private val mLocationRequest: LocationRequest = LocationRequest.create().apply {
        interval = UPDATE_INTERVAL_IN_MILLISECONDS
        fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        maxWaitTime = MAX_WAIT_TIME
    }
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                try {
                    mFusedLocationClient.lastLocation
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                mLocation = task.result
                                notify(NEW_LOCATION)
                                val broadcastIntent = Intent()
                                broadcastIntent.action = NEW_LOCATION
                                sendBroadcast(broadcastIntent)

                                if (baseContext.isLogged() && !eventQRid.isNullOrEmpty() && mLocation!=null) {
                                    CoroutineScope(Dispatchers.IO + Job()).launch {
                                        repository.pushPosition(eventQRid!!, baseContext.getAccountPassword(), mLocation!!.latitude, mLocation!!.longitude)
                                            .collect { networkResult ->
                                                networkResult.manage(onSuccess = {
                                                    Timber.d("PositionUpdated : ${it.result}")
                                                }, onError = {
                                                    Timber.e("PositionUpdated : network error ")
                                                })
                                            }
                                    }
                                }
                            } else {
                                Timber.w("Failed to get location.")
                            }
                        }
                } catch (unlikely: SecurityException) {
                    Timber.e("Lost location permission.$unlikely")
                }
            }
        }


    }


    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val action = intent?.action ?: "ERROR"
        val broadcastIntent = Intent()
        broadcastIntent.action = action
        sendBroadcast(broadcastIntent)

        if (STOP_SERVICE == action) {
            stopForeground(true)
            stopSelf()
        } else {
            notify(action)
            eventQRid = intent?.extras?.getString(EVENT_ID)
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
        return START_NOT_STICKY
    }


    private fun notify(action: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notifyCheckForeground = notificationManager.isNotifyForeground(FOREGROUND_NOTE_ID)

        Timber.v("Is foreground visible: $notifyCheckForeground")

        if (notifyCheckForeground) {
            notificationManager.notify(FOREGROUND_NOTE_ID, buildForegroundNotification(action))
        } else {
            startForeground(FOREGROUND_NOTE_ID, buildForegroundNotification(action))
        }
    }

    fun isNotifyForeground(): Boolean {
        return (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).isNotifyForeground(FOREGROUND_NOTE_ID)
    }

    fun lastLocation(): Location?{
        return mLocation
    }



    private fun NotificationManager.isNotifyForeground(id: Int): Boolean {
        val notifyCheckForeground = activeNotifications.filter {
            it.id == id
        }.any()
        return notifyCheckForeground
    }

    private fun buildForegroundNotification(_action: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        //Do any customization you want here
        val notificationContent: String = if (NEW_LOCATION == _action && mLocation!=null) {
            getString(R.string.label_location_update, mLocation!!.latitude, mLocation!!.longitude)
        } else {
            getString(R.string.label_start_share_position)
        }
        val notificationTitle = getString(R.string.app_name)
        val actionButtonText = getString(R.string.label_remove_location_updates)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "LocationService"
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                IMPORTANCE_LOW
            )
            serviceChannel.lightColor = ContextCompat.getColor(this, R.color.primary_color)
            serviceChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            manager.createNotificationChannel(serviceChannel)
            CHANNEL_ID
        }

        val intent = Intent(this, LocationForegroundService::class.java).apply {
            action = STOP_SERVICE
        }
        val pIntent = PendingIntent.getService(this, 112, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.PRIORITY_LOW)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.notification_mensa)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setOngoing(true)
            .setSound(null)
            .setVibrate(null)
            .addAction(android.R.drawable.ic_media_rew, actionButtonText, pIntent)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
        channel.lightColor = Color.RED
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(channel)
    }

    inner class LocationForegroundServiceBinder : Binder() {
        // Return this instance of MyService so clients can call public methods
        val service: LocationForegroundService
            get() =// Return this instance of MyService so clients can call public methods
                this@LocationForegroundService
    }


}