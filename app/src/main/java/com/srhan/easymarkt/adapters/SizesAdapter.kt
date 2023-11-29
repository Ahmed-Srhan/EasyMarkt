package com.srhan.easymarkt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.srhan.easymarkt.databinding.SizeRvItemBinding

class SizesAdapter : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {
    private var selectedPosition = -1
    var onItemClick: ((String) -> (Unit))? = null


    inner class SizesViewHolder(val itemBinding: SizeRvItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(size: String, position: Int) {
            itemBinding.apply {
                tvSize.text = size
                if (position == selectedPosition) { //Size is selected
                    imageShadow.visibility = View.VISIBLE

                } else { //Size is not selected
                    imageShadow.visibility = View.INVISIBLE
                }
            }
        }

    }


    private class ItemDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, ItemDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        return SizesViewHolder(
            SizeRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val sizes = differ.currentList[position]
        holder.bind(sizes, position)

        holder.itemBinding.imageColor.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(sizes)
        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}