package swaix.dev.mensaeventi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import swaix.dev.mensaeventi.databinding.ItemEventExtrasBinding
import swaix.dev.mensaeventi.databinding.ItemEventExtrasNoDataBinding
import swaix.dev.mensaeventi.model.EventItem
import swaix.dev.mensaeventi.utils.EMPTY_ROW

class EventExtraAdapter(private val onItemClick: (EventItem) -> Unit) : GenericAdapter<EventItem>(hasEmptyState = true) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBindViewHolder {
        return when (viewType) {
            EMPTY_ROW -> {
                EmptyExtraListViewHolder(ItemEventExtrasNoDataBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                EventExtraViewHolder(ItemEventExtrasBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: OnBindViewHolder, position: Int) {
        when (holder) {
            is EventExtraViewHolder -> {
                holder.onBind(getItem(position), onItemClick)
            }
        }
    }
}

class EmptyExtraListViewHolder(binding: ItemEventExtrasNoDataBinding) : OnBindViewHolder(binding)
class EventExtraViewHolder(binding: ItemEventExtrasBinding) : OnBindViewHolder(binding) {

    fun onBind(item: EventItem, onItemClick: (EventItem) -> Unit) {
        with(ItemEventExtrasBinding.bind(itemView)) {

            extraName.text = item.name
            itemView.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }


}