package swaix.dev.mensaeventi.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import swaix.dev.mensaeventi.utils.EMPTY_ROW
import swaix.dev.mensaeventi.utils.ITEM_ROW
import swaix.dev.mensaeventi.utils.LOADING_ROW

abstract class GenericAdapter<R>(private val hasEmptyState: Boolean = false, private val hasLoadingState: Boolean = false) : RecyclerView.Adapter<OnBindViewHolder>() {
    private val _dataSet: MutableList<R> = mutableListOf()
    var isLoading: Boolean = true

    final override fun getItemCount(): Int {
        return _dataSet.size + if (_dataSet.isEmpty() && (hasEmptyState || hasLoadingState)) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            hasLoadingState && isLoading -> LOADING_ROW
            hasEmptyState && !isLoading && _dataSet.isEmpty() -> EMPTY_ROW
            else -> ITEM_ROW
        }

    }

    fun getItem(position: Int): R = _dataSet[position]

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(list: List<R>) {
        isLoading = false
        _dataSet.clear()
        _dataSet.addAll(list)
        notifyDataSetChanged()
    }
}