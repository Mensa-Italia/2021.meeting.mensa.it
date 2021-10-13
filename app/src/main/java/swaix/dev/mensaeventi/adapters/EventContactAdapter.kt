package swaix.dev.mensaeventi.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.ItemContactDetailBinding
import swaix.dev.mensaeventi.databinding.ItemContactNameBinding
import swaix.dev.mensaeventi.databinding.ItemLoadingBinding
import swaix.dev.mensaeventi.databinding.ItemNoEventsBinding
import swaix.dev.mensaeventi.model.EventItem
import swaix.dev.mensaeventi.utils.*
import android.text.util.Linkify

import android.widget.TextView




class EventContactAdapter(private val hasEmptyState: Boolean = false, private val hasLoadingState: Boolean = false, private val onItemClick: (Item) -> Unit) : RecyclerView.Adapter<OnBindViewHolder>() {


    var isLoading: Boolean = true

    private val dataSet: MutableList<Item> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBindViewHolder {

        return when (viewType) {
            LOADING_ROW -> {
                val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingHolder(binding)

                //TODO SISTEMARE
            }
            EMPTY_ROW -> {
                val binding = ItemNoEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyListHolder(binding)
                //TODO SISTEMARE
            }
            HEADER_ITEM -> {
                val binding = ItemContactNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SealedContactBinding.ContactHeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemContactDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SealedContactBinding.DetailViewHolder(binding)
            }


        }
    }

    override fun onBindViewHolder(holder: OnBindViewHolder, position: Int) {
        when ((holder as SealedContactBinding)) {
            is SealedContactBinding.ContactHeaderViewHolder -> {
                holder.onBind(dataSet[position])
            }
            is SealedContactBinding.DetailViewHolder -> {
                holder.onBind(dataSet[position], onItemClick)
            }
        }
    }


    fun updateContacts(item: EventItem) {
        updateContacts(listOf(item))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateContacts(list: List<EventItem>) {
        dataSet.clear()
        list.forEach {
            dataSet.add(Item.Header(it.name, it.description, it.imageURL))
            if (it.link.isNotEmpty()) dataSet.add(Item.Link(it.name, it.description, it.imageURL, it.link))
            if (it.mail.isNotEmpty()) dataSet.add(Item.Email(it.name, it.description, it.imageURL, it.mail))
            if (it.telephoneNumber.isNotEmpty()) dataSet.add(Item.Telephone(it.name, it.description, it.imageURL, it.telephoneNumber))

        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSet.size + if (dataSet.isEmpty() && (hasEmptyState || hasLoadingState)) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            hasLoadingState && isLoading -> {
//                Timber.d("${this::class.java.name} getItemViewType: LOADING_ROW")
                LOADING_ROW
            }
            hasEmptyState && dataSet.isEmpty() -> {
//                Timber.d("${this::class.java.name} getItemViewType: EMPTY_ROW")
                EMPTY_ROW
            }
            dataSet.isNotEmpty() && dataSet[position] is Item.Header -> {
//                Timber.d("${this::class.java.name} getItemViewType: HEADER_ITEM")
                HEADER_ITEM
            }
            else -> {
//                Timber.d("${this::class.java.name} getItemViewType: ITEM_ROW")
                ITEM_ROW
            }
        }

    }
}

sealed class Item(
    val name: String,
    val description: String,
    val imageURL: String,
    val value: String,
) {
    class Header(name: String, description: String, imageURL: String) : Item(name, description, imageURL, "")
    class Telephone(name: String, description: String, imageURL: String, value: String) : Item(name, description, imageURL, value)
    class Email(name: String, description: String, imageURL: String, value: String) : Item(name, description, imageURL, value)
    class Link(name: String, description: String, imageURL: String, value: String) : Item(name, description, imageURL, value)
}

sealed class SealedContactBinding(binding: ViewBinding) : OnBindViewHolder(binding) {
    abstract fun onBind(item: Item, onclick: ((Item) -> Unit) = { })

    class ContactHeaderViewHolder(binding: ItemContactNameBinding) : SealedContactBinding(binding) {
        override fun onBind(item: Item, onclick: (Item) -> Unit) {
            with(ItemContactNameBinding.bind(itemView)) {

                contactName.text = item.name
            }
        }
    }

    class DetailViewHolder(binding: ItemContactDetailBinding) : SealedContactBinding(binding) {
        override fun onBind(item: Item, onclick: (Item) -> Unit) {
            with(ItemContactDetailBinding.bind(itemView)) {
                contactDetail.text = item.value

                when (item) {
                    is Item.Telephone -> {
                        image.setImageResource(R.drawable.ic_phone_call_icon)
                    }
                    is Item.Email -> image.setImageResource(R.drawable.ic_email_icon)
                    is Item.Header -> {
                    }
                    is Item.Link -> image.setImageResource(R.drawable.ic_web_icon)
                }
                root.setOnClickListener {
                    onclick(item)
                }
            }
        }
    }
}