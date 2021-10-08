package swaix.dev.mensaeventi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import swaix.dev.mensaeventi.databinding.PageCalendarBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import kotlin.time.ExperimentalTime

class EventCalendarAdapter(private val dataMap: Map<String, List<EventItemWithDate>>, private val infoClick: (EventItemWithDate) -> Unit, private val directionsClick: (EventItemWithDate) -> Unit) : RecyclerView.Adapter<PageCalendarHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageCalendarHolder {
        return PageCalendarHolder(PageCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @ExperimentalTime
    override fun onBindViewHolder(holder: PageCalendarHolder, position: Int) {
        val day = dataMap.keys.toTypedArray()[position]

        dataMap[day]?.let { activities ->
            holder.onBind(day, activities, infoClick, directionsClick)
        }
    }

    override fun getItemCount(): Int {
        return dataMap.size
    }

}

class PageCalendarHolder(binding: PageCalendarBinding) : OnBindViewHolder(binding) {
    @ExperimentalTime
    fun onBind(day: String, activities: List<EventItemWithDate>, onItemClick: (EventItemWithDate) -> Unit, directionsClick: (EventItemWithDate) -> Unit) {
        with(PageCalendarBinding.bind(itemView)) {
//            eventActivityList.adapter = EventActivityAdapter({
//                onItemClick(it)
//            }, {
//                directionsClick(it)
//            })
//            activities?.let { (eventActivityList.adapter as EventActivityAdapter).updateDataset(it) }
            timelineCalendar.data = activities
        }
    }
}