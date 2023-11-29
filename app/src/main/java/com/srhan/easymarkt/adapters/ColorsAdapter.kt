package com.srhan.easymarkt.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.srhan.easymarkt.databinding.ColorRvItemBinding

class ColorsAdapter : RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>() {
    private var selectedPosition = -1
    var onItemClick: ((Int) -> (Unit))? = null


    inner class ColorsViewHolder(val itemBinding: ColorRvItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(colors: Int, position: Int) {
            itemBinding.apply {
                val imageDrawable = ColorDrawable(colors)
                imageColor.setImageDrawable(imageDrawable)
                if (position == selectedPosition) { //Color is selected
                    imageShadow.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE

                } else { //Color is not selected
                    imageShadow.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE
                }
            }
        }

    }


    private class ItemDiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, ItemDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        return ColorsViewHolder(
            ColorRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        val colors = differ.currentList[position]
        holder.bind(colors, position)

        holder.itemBinding.imageColor.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(colors)
        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}