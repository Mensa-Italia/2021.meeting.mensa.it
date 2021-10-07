package swaix.dev.mensaeventi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import swaix.dev.mensaeventi.databinding.ItemEventActivityBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.utils.asHtml
import swaix.dev.mensaeventi.utils.hourMinuteString

class EventActivityAdapter(private val infoClick: (EventItemWithDate) -> Unit, private val directionsClick: (EventItemWithDate) -> Unit) : GenericAdapter<EventItemWithDate>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBindViewHolder {
        return ActivityViewHolder(ItemEventActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OnBindViewHolder, position: Int) {
        when (holder) {
            is ActivityViewHolder -> {
                holder.onBind(getItem(position), infoClick, directionsClick)
            }
        }
    }
}


class ActivityViewHolder(binding: ItemEventActivityBinding) : OnBindViewHolder(binding) {
    fun onBind(item: EventItemWithDate, onItemClick: (EventItemWithDate) -> Unit, directionsClick: (EventItemWithDate) -> Unit) {
        with(ItemEventActivityBinding.bind(itemView)) {
            timeFrom.text = item.dateFrom.hourMinuteString()
            timeTo.text = item.dateTo.hourMinuteString()
            eventActivityName.text = item.name.asHtml()
            eventActivityDescrition.text = item.description.asHtml()
            reminder.setOnClickListener {
                onItemClick(item)
            }
            directions.setOnClickListener {
                directionsClick(item)
            }
        }
    }
}