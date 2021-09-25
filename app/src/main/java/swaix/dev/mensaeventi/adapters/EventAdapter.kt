package swaix.dev.mensaeventi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import swaix.dev.mensaeventi.databinding.ItemEventBinding
import swaix.dev.mensaeventi.databinding.ItemLoadingBinding
import swaix.dev.mensaeventi.databinding.ItemNoEventsBinding
import swaix.dev.mensaeventi.databinding.ItemPlaceholderBinding
import swaix.dev.mensaeventi.model.MensaEvent
import swaix.dev.mensaeventi.utils.*

class EventAdapter(private val onItemClick: (MensaEvent) -> Unit) : RecyclerView.Adapter<OnBindViewHolder>() {
    private val dataSet: MutableList<MensaEvent> = mutableListOf()
    var isLoading: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBindViewHolder {
        return when (viewType) {
            FIRST_ROW_SPACE -> {
                val binding = ItemPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PlaceHolder(binding)
            }
            EMPTY_ROW ->{
                val binding = ItemNoEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyListHolder(binding)
            }
            LOADING_ROW ->{
                val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingHolder(binding)
            }
            else ->  {
                val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EventViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isLoading -> LOADING_ROW
            !isLoading && dataSet.isEmpty() -> EMPTY_ROW
            !isLoading && dataSet.isNotEmpty() && position==0 -> FIRST_ROW_SPACE
            else -> ITEM_ROW
        }

    }

    override fun onBindViewHolder(holder: OnBindViewHolder, position: Int) {
        when (holder) {
            is EventViewHolder -> {
                dataSet.getOrNull(position - 1)?.let {
                    holder.onBind(it, onItemClick)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.count() + 1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(list: List<MensaEvent>) {
        isLoading = false
        dataSet.clear()
        dataSet.addAll(list)
        notifyDataSetChanged()
    }

}

abstract class OnBindViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

class PlaceHolder(binding: ItemPlaceholderBinding) : OnBindViewHolder(binding)
class LoadingHolder(binding: ItemLoadingBinding) : OnBindViewHolder(binding)
class EmptyListHolder(binding: ItemNoEventsBinding) : OnBindViewHolder(binding)
class EventViewHolder(binding: ItemEventBinding) : OnBindViewHolder(binding) {

    fun onBind(item: MensaEvent, onItemClick: (MensaEvent) -> Unit) {
        with(ItemEventBinding.bind(itemView)) {
            val dateFrom = item.dateFrom
            val dateTo = item.dateTo
            val context = itemView.context
            val message = context.formatDateRange(dateFrom, dateTo)
            dateLabel.text = message
            eventCity.text = item.name
            itemView.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }


}