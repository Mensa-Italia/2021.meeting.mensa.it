package swaix.dev.mensaeventi.utils

import android.widget.TextView
import androidx.annotation.DrawableRes

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}