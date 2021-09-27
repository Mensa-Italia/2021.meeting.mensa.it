package swaix.dev.mensaeventi.adapters

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import swaix.dev.mensaeventi.utils.EMPTY_ROW
import swaix.dev.mensaeventi.utils.ITEM_ROW
import swaix.dev.mensaeventi.utils.LOADING_ROW
import swaix.dev.mensaeventi.utils.TAG

abstract class GenericAdapter<R>(private val hasEmptyState: Boolean = false, private val hasLoadingState: Boolean = false) : RecyclerView.Adapter<OnBindViewHolder>() {
    private val _dataSet: MutableList<R> = mutableListOf()
    var isLoading: Boolean = true

    final override fun getItemCount(): Int {
        return _dataSet.size + if (_dataSet.isEmpty() && (hasEmptyState || hasLoadingState)) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            hasLoadingState && isLoading -> {
                Log.d(TAG, "${this@GenericAdapter::class.java.name} getItemViewType: LOADING_ROW")
                LOADING_ROW
            }
            hasEmptyState && _dataSet.isEmpty() -> {
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
        return _dataSet[position]
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(list: List<R>) {
        isLoading = false
        _dataSet.clear()
        _dataSet.addAll(list)
        notifyDataSetChanged()
    }
}