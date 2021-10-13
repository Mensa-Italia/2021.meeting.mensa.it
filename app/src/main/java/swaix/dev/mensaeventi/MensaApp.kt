package swaix.dev.mensaeventi

import android.app.Application
import android.content.Context
import com.bumptech.glide.annotation.GlideModule
import dagger.hilt.android.HiltAndroidApp
import swaix.dev.mensaeventi.utils.ReleaseTree
import timber.log.Timber
import timber.log.Timber.DebugTree


@HiltAndroidApp
class MensaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}