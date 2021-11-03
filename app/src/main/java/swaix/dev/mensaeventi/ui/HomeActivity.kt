package swaix.dev.mensaeventi.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.ActivityHomeBinding
import swaix.dev.mensaeventi.utils.LocationForegroundService.Companion.NEW_LOCATION
import swaix.dev.mensaeventi.utils.LocationForegroundService.Companion.START_SERVICE
import swaix.dev.mensaeventi.utils.LocationForegroundService.Companion.STOP_SERVICE


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val baseViewModel: BaseViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction(START_SERVICE)
        mIntentFilter.addAction(STOP_SERVICE)
        mIntentFilter.addAction(NEW_LOCATION)
        registerReceiver(mReceiver, mIntentFilter)


//        enableClicks()
        with(binding) {
            baseViewModel.showLoading.observe(this@HomeActivity, {
                loading.visibility = if (it) View.VISIBLE else View.GONE
            })
        }

    }


    // region animation COMMENTATA FINCHE NON VERRA IMPLEMENTATO IL LOGIN NON CANCELLARE!!
//    private inline fun performAnimation(crossinline block: suspend () -> Unit) {
//        lifecycleScope.launch {
//            disableClicks()
//            block()
//            enableClicks()
//        }
//    }
//
//
//    private fun disableClicks() {
//        with(binding) {
//            closeUser.setOnClickListener(null)
//            userFab.setOnClickListener(null)
//        }
//
//    }
//
//
//    private fun enableClicks() = with(binding) {
//        when (root.currentState) {
//            R.id.fab_closed -> {
//                userFab.setOnClickListener {
//                    root.openUser()
//                }
//            }
//            R.id.fab_open -> {
//                closeUser.setOnClickListener {
//                    root.closeUser()
//                }
//            }
//            else -> {
//            }
////        throw IllegalStateException("Can be called only for the permitted 3 currentStates")
//        }
//    }
//
//    private fun MotionLayout.closeUser(): Unit = performAnimation {
//        transitionToStart()
//        awaitTransitionComplete(R.id.fab_middle)
//        setTransition(R.id.fab_closed, R.id.fab_middle)
//        progress = 1f
//        transitionToStart()
//        awaitTransitionComplete(R.id.fab_closed)
//    }
//
//
//    private fun MotionLayout.openUser(): Unit = performAnimation {
//        setTransition(R.id.fab_closed, R.id.fab_middle)
//        transitionToState(R.id.fab_middle)
//        awaitTransitionComplete(R.id.fab_middle)
//        transitionToState(R.id.fab_open)
//        awaitTransitionComplete(R.id.fab_open)
//    }
//
//
//    private suspend fun awaitTransitionComplete(transitionId: Int, timeout: Long = 10000L) {
//        with(binding.root) {
//            kotlinx.coroutines.withTimeout(timeout) {
//                kotlinx.coroutines.suspendCancellableCoroutine<Unit> { continuation ->
//                    val listener = object : MotionLayout.TransitionListener {
//                        override fun onTransitionTrigger(
//                            p0: MotionLayout?,
//                            p1: Int,
//                            p2: Boolean,
//                            p3: Float
//                        ) {
//
//                        }
//
//                        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
//
//                        }
//
//                        override fun onTransitionChange(
//                            p0: MotionLayout?,
//                            p1: Int,
//                            p2: Int,
//                            p3: Float
//                        ) {
//
//                        }
//
//                        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
//                            if (p1 == transitionId) {
//                                setTransitionListener(null)
//                                continuation.resume(kotlin.Unit) {
//
//                                }
//                            }
//                        }
//
//                    }
//                    try {
//
//                        continuation.invokeOnCancellation {
//                            setTransitionListener(null)
//                        }
//                        setTransitionListener(listener)
//                    } catch (tex: TimeoutCancellationException) {
//                        setTransitionListener(null)
//                        throw kotlinx.coroutines.CancellationException(
//                            "Transition to state with id: $transitionId did not" +
//                                    " complete in timeout.", tex
//                        )
//                    }
//                }
//            }
//        }
//    }

    // endregion

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action) {
                START_SERVICE -> {
                    baseViewModel.buttonShareText.postValue(SharePositionData(getString(R.string.label_sharing_loading), true))
                }
                STOP_SERVICE -> {
                    baseViewModel.buttonShareText.postValue(SharePositionData(getString(R.string.label_share_position), false))
                }
                NEW_LOCATION -> {
                    baseViewModel.buttonShareText.postValue(SharePositionData(getString(R.string.label_shared_position), true))
                }
            }
        }

    }
}