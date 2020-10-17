package com.rujul.calendaractivity.base

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseBindingViewHolder(var binding: ViewDataBinding, var clickListener: ClickListener) :
    RecyclerView.ViewHolder(
        binding.root
    ), View.OnClickListener {
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        clickListener.onViewClick(v!!, adapterPosition)
    }

    init {
        binding.root.setOnClickListener(this)
        clickViews(this)
    }

    private fun clickViews(bindingViewHolder: BaseBindingViewHolder) {
        val view = bindingViewHolder.binding.root
        if (view is ViewGroup) {
            setViewGroupClick(view, view)
        }
    }

    private fun setViewGroupClick(viewGroup: ViewGroup, parentView: View) {
        for (i in 0 until viewGroup.childCount) {
            if (viewGroup.childCount > 0 && viewGroup.getChildAt(i) is ViewGroup) {
                setViewGroupClick(viewGroup.getChildAt(i) as ViewGroup, parentView)
            }
            viewGroup.getChildAt(i)
                .setOnClickListener { view -> clickListener.onViewClick(view!!, adapterPosition) }
        }
    }

    interface ClickListener {
        fun onViewClick(view: View, position: Int)
    }
}