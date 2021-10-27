package swaix.dev.mensaeventi.model

import com.google.gson.annotations.SerializedName

data class ResponseIsUserCheckedIn(val isCheckedIn: Boolean, @SerializedName("idEvento") val eventId: String )