package com.codefal.mystoryapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codefal.mystoryapp.databinding.FragmentStoryBinding
import com.codefal.mystoryapp.network.model.ListStoryItem

class MyItemAdapter: PagingDataAdapter<ListStoryItem, MyItemAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onClick : ((ListStoryItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.setItem(item)
            holder.binding.cardItem.setOnClickListener {
                onClick?.invoke(item)

            }
        }
    }
    inner class ViewHolder(val binding: FragmentStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ListStoryItem){
            binding.apply {
                Glide.with(itemView).load(item.photoUrl).into(ivItemPhoto)
                tvItemName.text = item.name
            }
        }
    }
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}