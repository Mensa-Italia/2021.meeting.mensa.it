package swaix.dev.mensaeventi.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.adapters.Item
import swaix.dev.mensaeventi.api.NetworkResult
import kotlin.random.Random


fun Int.Companion.randomColor(): Int {
    return Color.argb(
        255,
        Random.nextInt(256),
        Random.nextInt(256),
        Random.nextInt(256)
    )
}

fun Context.getIdResByName(name: String): Int {
    return resources.getIdentifier(name, "id", packageName)
}

fun Fragment.setContactClickListener(it: Item) {
    when (it) {
        is Item.Email -> {
            requireContext().sendEmail(it.value, null, null)
        }
        is Item.Header -> {
            // NO EVENT
        }
        is Item.Link -> {

            (it.value).openInBrowser(requireContext())

        }
        is Item.Telephone -> {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${it.value}")
            requireContext().startActivity(intent)
        }
    }
}

fun Context.sendEmail(
    address: String?,
    subject: String?,
    body: String?,
) {
    val selectorIntent = Intent(ACTION_SENDTO)
        .setData("mailto:$address".toUri())
    val emailIntent = Intent(ACTION_SEND).apply {
        putExtra(EXTRA_EMAIL, arrayOf(address))
        putExtra(EXTRA_SUBJECT, subject)
        putExtra(EXTRA_TEXT, body)
        selector = selectorIntent
    }
    startActivity(Intent.createChooser(emailIntent, getString(R.string.email_title)))

}


fun String.openInBrowser(context: Context) {
    try {
        var webpage = Uri.parse(this)
        if (!startsWith("http://") && !startsWith("https://")) {
            webpage = Uri.parse("http://$this")
        }
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Errore durante l'apertura del link", Toast.LENGTH_LONG).show()
    }
}


fun Context.hasPermissions(vararg permissions: String) = permissions.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}


fun <T> NetworkResult<T>.manage( onLoading: ()->Unit = {}, onSuccess: (T) -> Unit, onError: () -> Unit = {}) {
    when (this) {
        is NetworkResult.Success -> {
            data?.let { value ->
                onSuccess.invoke(value)
            }
        }
        is NetworkResult.Error -> {
            onError.invoke()
        }
        is NetworkResult.Loading ->{
            onLoading.invoke()
        }
    }
}