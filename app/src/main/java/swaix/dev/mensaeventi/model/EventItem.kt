package swaix.dev.mensaeventi.model

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
) : Serializable, Comparable<EventItem>, Searchable {

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

    override fun compareTo(other: EventItem): Int = id.compareTo(other.id)
    override fun search(value: String): Boolean {
        return when {
            this.id.toString().contains(value) -> true
            this.name.contains(value) -> true
            this.description.contains(value) -> true
            this.mail.contains(value) -> true
            this.telephoneNumber.contains(value) -> true
            else -> false
        }
    }

}