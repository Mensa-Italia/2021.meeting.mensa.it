package swaix.dev.mensaeventi.utils

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

interface AutoUpdatableAdapter {

    fun <T > RecyclerView.Adapter<*>.autoNotify(oldList: List<T>, newList: List<T>) where T : Comparable<T>, T: Searchable {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].compareTo(newList[newItemPosition]) == 0
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return  oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })

        diff.dispatchUpdatesTo(this)
    }

}

interface Searchable{
    fun search(value: String): Boolean
}