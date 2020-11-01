package com.rujul.calendaractivity.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBindingAdapter<T> : RecyclerView.Adapter<BaseBindingViewHolder>(),
    BaseBindingViewHolder.ClickListener {

    override fun onViewClick(view: View, position: Int) {
        itemClickListener?.onItemClick(view, items[position], position)
    }

    /**
     * Enable filter or not !
     */
    var filterable: Boolean = false

    protected var items: ArrayList<T> = ArrayList<T>()

    /**
     * used for backup purpose in case of filterable = true
     */
    protected var allItems: ArrayList<T> = ArrayList<T>()

    var itemClickListener: ItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = bind(inflater, parent, viewType)
        return BaseBindingViewHolder(binding, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    public fun getItemsArray() = items

    fun setItem(item: ArrayList<T>) {
        items = item
        duplicateItems(items)
        duplicateMaps(items)
        if (filterable) allItems = item
        notifyDataSetChanged()
    }

    abstract fun duplicateItems(items: ArrayList<T>)

    abstract fun duplicateMaps(items: ArrayList<T>)

    fun addItems(item: ArrayList<T>) {
        items.addAll(item)
        if (filterable) allItems.addAll(item)
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        items.add(item)
        if (filterable) allItems.add(item)
        notifyDataSetChanged()
    }

    fun addItemNotify(item: T) {
        items.add(item)
        if (filterable) allItems.add(item)
        notifyItemInserted(itemCount)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        if (filterable) allItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        val size = items.size
        if (size > 0) {
            items.clear()
            if (filterable) allItems.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    protected abstract fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding

    interface ItemClickListener<T> {
        fun onItemClick(view: View, data: T, position: Int)
    }
}