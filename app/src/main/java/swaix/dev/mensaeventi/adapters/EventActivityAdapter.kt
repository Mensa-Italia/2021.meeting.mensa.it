package swaix.dev.mensaeventi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import swaix.dev.mensaeventi.databinding.ItemEventActivityBinding
import swaix.dev.mensaeventi.model.MensaEventActivity
import swaix.dev.mensaeventi.utils.hourMinuteString

class EventActivityAdapter(private val onItemClick: (MensaEventActivity) -> Unit) : GenericAdapter<MensaEventActivity>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBindViewHolder {
        return ActivityViewHolder(ItemEventActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OnBindViewHolder, position: Int) {
        when (holder) {
            is ActivityViewHolder -> {
                holder.onBind(getItem(position), onItemClick)
            }
        }
    }
}


class ActivityViewHolder(binding: ItemEventActivityBinding) : OnBindViewHolder(binding) {
    fun onBind(item: MensaEventActivity, onItemClick: (MensaEventActivity) -> Unit) {
        with(ItemEventActivityBinding.bind(itemView)) {
            timeFrom.text = item.dateFrom.hourMinuteString()
            timeTo.text = item.dateTo.hourMinuteString()
            eventActivityName.text = item.name
            eventActivityDescrition.text = item.description
            reminder.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}