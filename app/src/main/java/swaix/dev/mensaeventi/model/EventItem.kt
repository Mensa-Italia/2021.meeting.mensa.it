package swaix.dev.mensaeventi.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import swaix.dev.mensaeventi.utils.Searchable
import java.io.Serializable

open class EventItem(
    val id: Int,
    val name: String,
    val description: String,
    val type: ItemType,
    val imageURL: String,
    val position: Position,
    val mail: String,
    val link: String,
    val telephoneNumber: String,
    var columnOnCalendar : Int = 0,
) : Serializable, Comparable<EventItem>, Searchable, ClusterItem {

    companion object{
        fun getEmpty():EventItem{
            return EventItem(
                -1,
                "",
                "",
                ItemType.NONE,
                "",
                Position(),
                "",
                "",
                "",
            )
        }
    }

    override fun getPosition(): LatLng {
        return LatLng(position.latitude, position.longitude)
    }

    override fun getTitle(): String? {
        return name
    }

    override fun getSnippet(): String {
        return id.toString()
    }

    override fun compareTo(other: EventItem): Int = id.compareTo(other.id)
    override fun search(value: String): Boolean {
        val upperCase = value.uppercase()
        return when {
            this.id.toString().contains(upperCase) -> true
            this.name.uppercase().contains(upperCase) -> true
            this.description.uppercase().contains(upperCase) -> true
            this.mail.uppercase().contains(upperCase) -> true
            this.telephoneNumber.uppercase().contains(upperCase) -> true
            else -> false
        }
    }

}