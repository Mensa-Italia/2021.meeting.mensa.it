package swaix.dev.mensaeventi.utils

import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}
fun View.vibrate() {
    performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
}