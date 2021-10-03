package swaix.dev.mensaeventi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import swaix.dev.mensaeventi.databinding.ItemEventBinding
import swaix.dev.mensaeventi.databinding.ItemLoadingBinding
import swaix.dev.mensaeventi.databinding.ItemNoEventsBinding
import swaix.dev.mensaeventi.databinding.ItemPlaceholderBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.utils.EMPTY_ROW
import swaix.dev.mensaeventi.utils.FIRST_ROW_SPACE
import swaix.dev.mensaeventi.utils.LOADING_ROW
import swaix.dev.mensaeventi.utils.formatDateRange

import android.graphics.drawable.Drawable

import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

import com.bumptech.glide.request.RequestListener
import swaix.dev.mensaeventi.R


class EventAdapter(private val onItemClick: (EventItemWithDate) -> Unit) : GenericAdapter<EventItemWithDate>(hasEmptyState = true, hasLoadingState = true) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBindViewHolder {
        return when (viewType) {
            FIRST_ROW_SPACE -> {
                val binding = ItemPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PlaceHolder(binding)
            }
            EMPTY_ROW -> {
                val binding = ItemNoEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyListHolder(binding)
            }
            LOADING_ROW -> {
                val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingHolder(binding)
            }
            else -> {
                val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EventViewHolder(binding)
            }
        }
    }


    override fun onBindViewHolder(holder: OnBindViewHolder, position: Int) {
        when (holder) {
            is EventViewHolder -> {
                holder.onBind(getItem(position), onItemClick)
            }
        }
    }


}

class PlaceHolder(binding: ItemPlaceholderBinding) : OnBindViewHolder(binding)
class LoadingHolder(binding: ItemLoadingBinding) : OnBindViewHolder(binding)
class EmptyListHolder(binding: ItemNoEventsBinding) : OnBindViewHolder(binding)
class EventViewHolder(binding: ItemEventBinding) : OnBindViewHolder(binding) {

    fun onBind(item: EventItemWithDate, onItemClick: (EventItemWithDate) -> Unit) {
        with(ItemEventBinding.bind(itemView)) {
            val dateFrom = item.dateFrom
            val dateTo = item.dateTo
            val context = itemView.context
            val message = context.formatDateRange(dateFrom, dateTo)
            dateLabel.text = message
            eventCity.text = item.name



            Glide.with(itemView.context)
                .load(item.imageURL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(),RoundedCorners(36))
                .into(imageEvent)

            itemView.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }


}