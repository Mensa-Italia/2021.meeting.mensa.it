package swaix.dev.mensaeventi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import swaix.dev.mensaeventi.databinding.ItemEventBinding
import swaix.dev.mensaeventi.model.EventsDetails
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(private val onItemClick: (EventsDetails) -> Unit) : RecyclerView.Adapter<EventViewHolder>() {
    private val dataSet: MutableList<EventsDetails> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = dataSet[position]
        holder.onBind(event, onItemClick)
    }

    override fun getItemCount(): Int {
        return dataSet.count()
    }

    fun updateDataset(list: List<EventsDetails>) {
        dataSet.clear()
        dataSet.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }
}

class EventViewHolder(binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

    private val formatDate = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
    private val formatTime = SimpleDateFormat("HH:ss", Locale.getDefault())
    fun onBind(event: EventsDetails, onItemClick: (EventsDetails) -> Unit) {
        with(ItemEventBinding.bind(itemView)) {
            date.text = formatDate.format(event.date)
            time.text = formatTime.format(event.date)
            name.text = event.title
            itemView.setOnClickListener {
                onItemClick.invoke(event)
            }
        }
    }
}