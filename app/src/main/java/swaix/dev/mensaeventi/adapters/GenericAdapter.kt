package swaix.dev.mensaeventi.adapters

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import swaix.dev.mensaeventi.utils.*
import kotlin.properties.Delegates

abstract class GenericAdapter<R>(private val hasEmptyState: Boolean = false, private val hasLoadingState: Boolean = false) : RecyclerView.Adapter<OnBindViewHolder>(), AutoUpdatableAdapter where R : Comparable<R>, R : Searchable {
    private var _dataSet: List<R> by Delegates.observable(mutableListOf()) { _, _, newList ->
        items = newList.toMutableList()
    }

    var isLoading: Boolean = true

    private var items: List<R> by Delegates.observable(mutableListOf()) { _, oldList, newList ->
        if (oldList.any())
            autoNotify(oldList, newList)
        else {
            notifyDataSetChanged()
        }
    }

    final override fun getItemCount(): Int {
        return if (items.any()) items.size else if (hasEmptyState || hasLoadingState) 1 else 0
        // return items.size + if (items.isEmpty() && (hasEmptyState || hasLoadingState)) 1 else 0
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            hasLoadingState && isLoading -> {
                Log.d(TAG, "${this@GenericAdapter::class.java.name} getItemViewType: LOADING_ROW")
                LOADING_ROW
            }
            hasEmptyState && items.isEmpty() -> {
                Log.d(TAG, "${this@GenericAdapter::class.java.name} getItemViewType: EMPTY_ROW")
                EMPTY_ROW
            }
            else -> {
                Log.d(TAG, "${this@GenericAdapter::class.java.name} getItemViewType: ITEM_ROW")
                ITEM_ROW
            }
        }

    }

    fun getItem(position: Int): R {
        return items[position]
    }


    fun filter(value: String) {
        items = _dataSet.filter {
            it.search(value)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(list: List<R>) {
        isLoading = false
        _dataSet = list
    }
}