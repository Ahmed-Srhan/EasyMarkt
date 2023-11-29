package com.srhan.easymarkt.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srhan.easymarkt.databinding.ViewpagerImageItemBinding

class ViewPager2ImagesAdapter :
    RecyclerView.Adapter<ViewPager2ImagesAdapter.ViewPager2ImagesViewHolder>() {

    private class ItemDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, ItemDiffCallback())

    /////////////////////////////////////////////////////////////////
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImagesViewHolder {
        return ViewPager2ImagesViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewPager2ImagesViewHolder, position: Int) {
        val path = differ.currentList[position]
        holder.bind(path)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewPager2ImagesViewHolder(private val itemBinding: ViewpagerImageItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(pathImage: String) {
            Glide.with(itemView).load(pathImage).into(itemBinding.imageProductDetails)

        }

    }


}
