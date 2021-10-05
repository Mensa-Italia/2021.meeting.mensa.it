package swaix.dev.mensaeventi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.ItemEventExtrasBinding
import swaix.dev.mensaeventi.databinding.ItemEventExtrasNoDataBinding
import swaix.dev.mensaeventi.model.EventItem
import swaix.dev.mensaeventi.model.ItemType
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

            Glide.with(itemView.context)
                .load(item.imageURL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(50))
                .into(background)

            when(item.type){
                ItemType.HOTEL -> {
                    extraName.background = ContextCompat.getDrawable(itemView.context, R.drawable.badge_hotel_text_background)
                    badge.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.badge_hotel_icon))
                }
                ItemType.RESTAURANT -> {
                    extraName.background = ContextCompat.getDrawable(itemView.context, R.drawable.badge_restaurant_text_background)
                    badge.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.badge_restaurant_icon))
                }
                else ->{}
            }

            itemView.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }


}