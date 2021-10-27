package swaix.dev.mensaeventi.utils

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.NetworkErrorException
import android.content.Context
import android.os.Bundle


class CustomAuthenticator(context: Context?) : AbstractAccountAuthenticator(context) {
    @Throws(NetworkErrorException::class)
    override fun addAccount(accountAuthenticatorResponse: AccountAuthenticatorResponse, s: String, s2: String, strings: Array<String>, bundle: Bundle): Bundle? {
        return null//To change body of implemented methods use File | Settings | File Templates.
    }

    override fun editProperties(accountAuthenticatorResponse: AccountAuthenticatorResponse, s: String): Bundle? {
        return null //To change body of implemented methods use File | Settings | File Templates.
    }

    @Throws(NetworkErrorException::class)
    override fun confirmCredentials(accountAuthenticatorResponse: AccountAuthenticatorResponse, account: Account, bundle: Bundle): Bundle? {
        return null //To change body of implemented methods use File | Settings | File Templates.
    }

    @Throws(NetworkErrorException::class)
    override fun getAuthToken(accountAuthenticatorResponse: AccountAuthenticatorResponse, account: Account, s: String, bundle: Bundle): Bundle? {
        return null //To change body of implemented methods use File | Settings | File Templates.
    }

    override fun getAuthTokenLabel(s: String): String? {
        return null //To change body of implemented methods use File | Settings | File Templates.
    }

    @Throws(NetworkErrorException::class)
    override fun updateCredentials(accountAuthenticatorResponse: AccountAuthenticatorResponse, account: Account, s: String, bundle: Bundle): Bundle? {
        return null //To change body of implemented methods use File | Settings | File Templates.
    }

    @Throws(NetworkErrorException::class)
    override fun hasFeatures(accountAuthenticatorResponse: AccountAuthenticatorResponse, account: Account, strings: Array<String>): Bundle? {
        return null //To change body of implemented methods use File | Settings | File Templates.
    }
}