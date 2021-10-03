package swaix.dev.mensaeventi.ui

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.ActivityHomeBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_home)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home/*, R.id.navigation_dashboard, R.id.navigation_notifications*/
//            )
//        )
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)


//        val baseViewModel: BaseViewModel by viewModels()



        enableClicks()
        with(binding.root) {
            setTransition(R.id.fab_closed, R.id.fab_middle)
        }
    }

    // region animation
    private inline fun performAnimation(crossinline block: suspend () -> Unit) {
        lifecycleScope.launch {
            disableClicks()
            block()
            enableClicks()
        }
    }


    private fun disableClicks() {
        with(binding) {
            closeUser.setOnClickListener(null)
            userFab.setOnClickListener(null)
        }

    }


    private fun enableClicks() = with(binding) {
        when (root.currentState) {
            R.id.fab_closed -> {
                userFab.setOnClickListener {
                    root.openUser()
                }
            }
            R.id.fab_open -> {
                closeUser.setOnClickListener {
                    root.closeUser()
                }
            }
            else -> {
            }
//        throw IllegalStateException("Can be called only for the permitted 3 currentStates")
        }
    }

    private fun MotionLayout.closeUser(): Unit = performAnimation {
        transitionToStart()
        awaitTransitionComplete(R.id.fab_middle)
        setTransition(R.id.fab_closed, R.id.fab_middle)
        progress = 1f
        transitionToStart()
        awaitTransitionComplete(R.id.fab_closed)
    }


    private fun MotionLayout.openUser(): Unit = performAnimation {
        setTransition(R.id.fab_closed, R.id.fab_middle)
        transitionToState(R.id.fab_middle)
        awaitTransitionComplete(R.id.fab_middle)
        transitionToState(R.id.fab_open)
        awaitTransitionComplete(R.id.fab_open)
    }


    private suspend fun awaitTransitionComplete(transitionId: Int, timeout: Long = 10000L) {
        with(binding.root) {
            kotlinx.coroutines.withTimeout(timeout) {
                kotlinx.coroutines.suspendCancellableCoroutine<Unit> { continuation ->
                    val listener = object : MotionLayout.TransitionListener {
                        override fun onTransitionTrigger(
                            p0: MotionLayout?,
                            p1: Int,
                            p2: Boolean,
                            p3: Float
                        ) {

                        }

                        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

                        }

                        override fun onTransitionChange(
                            p0: MotionLayout?,
                            p1: Int,
                            p2: Int,
                            p3: Float
                        ) {

                        }

                        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                            if (p1 == transitionId) {
                                setTransitionListener(null)
                                continuation.resume(kotlin.Unit) {

                                }
                            }
                        }

                    }
                    try {

                        continuation.invokeOnCancellation {
                            setTransitionListener(null)
                        }
                        setTransitionListener(listener)
                    } catch (tex: TimeoutCancellationException) {
                        setTransitionListener(null)
                        throw kotlinx.coroutines.CancellationException(
                            "Transition to state with id: $transitionId did not" +
                                    " complete in timeout.", tex
                        )
                    }
                }
            }
        }
    }
}