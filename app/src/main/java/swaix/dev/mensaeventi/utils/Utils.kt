package swaix.dev.mensaeventi.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import swaix.dev.mensaeventi.adapters.Item

fun Fragment.setContactClickListener(it: Item) {
    when (it) {
        is Item.Email -> {
            Toast.makeText(requireContext(), "gestire apertura email", Toast.LENGTH_LONG).show()
        }
        is Item.Header -> {
            // NO EVENT
        }
        is Item.Link -> {
            Toast.makeText(requireContext(), "gestire apertura link esterno", Toast.LENGTH_LONG).show()
        }
        is Item.Telephone -> {
            Toast.makeText(requireContext(), "gestire apertura telefonata", Toast.LENGTH_LONG).show()
        }
    }
}