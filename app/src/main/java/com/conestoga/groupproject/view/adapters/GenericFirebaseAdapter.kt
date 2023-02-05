package com.conestoga.groupproject.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.conestoga.groupproject.BR
import com.conestoga.groupproject.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

open class ViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Any) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}

class GenericFirebaseAdapter<T : Any, B : ViewDataBinding>(
    options: FirebaseRecyclerOptions<T>,
    @LayoutRes private val layoutId: Int,
    private val itemClickListener: (T) -> Unit,
) : FirebaseRecyclerAdapter<T, ViewHolder<B>>(options) {
    // Will be implemented in the next steps...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
        return ViewHolder<B>(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        ).apply {
            binding
                .root
                .findViewById<View>(R.id.itemClickable)
                .setOnClickListener {
                    itemClickListener.invoke(getItem(adapterPosition))
                }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int, model: T) {
        holder.bind(model)
    }
}



class WrapContentLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("TAG", "meet a IOOBE in RecyclerView")
        }
    }
}