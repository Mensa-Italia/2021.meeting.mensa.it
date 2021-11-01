package swaix.dev.mensaeventi.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import swaix.dev.mensaeventi.R
import timber.log.Timber

const val NAME = "name"
const val SURNAME = "surname"
const val MENSA_ID = "mensaId"
const val EVENT_ID = "eventId"


fun Context.accountManager(): AccountManager {
    return AccountManager.get(this)
}

fun Context.getAccount(): Account? {
    return with(accountManager()) {
        getAccountsByType(getString(R.string.account)).getOrNull(0)
    }
}

fun Context.isLogged(): Boolean {
    return getAccount() != null
}

fun Context.getAccountPassword(): String {
    with(accountManager()) {
        return getPassword(getAccount())
    }
}

fun Context.createAccount(name: String, surname: String, mensaId: String, onAccountCreated: () -> Any = {}) {
    with(accountManager()) {
        Account(name, getString(R.string.account)).let { account ->
            bundleOf(NAME to name, SURNAME to surname, MENSA_ID to mensaId).let { bundle ->
                if (this.addAccountExplicitly(account, mensaId, bundle)) {
                    Timber.w("utente loggato")
                    onAccountCreated.invoke()
                }
            }
        }
    }
}

suspend fun Fragment.deleteAccount() {
    lifecycleScope.launch {
        with(requireActivity().accountManager()) {
            accounts.forEach {
                removeAccount(it, requireActivity(), {

                }, null)
            }
        }
    }
}

fun Context.setData(key: String, value: String) {
    with(accountManager()) {
        getAccount()?.let {
            setUserData(it, key, value)
        }
    }
}

fun Context.getAccountData(key: String): String? {
    return getAccount()?.let { accountManager().getUserData(it, key) }
}