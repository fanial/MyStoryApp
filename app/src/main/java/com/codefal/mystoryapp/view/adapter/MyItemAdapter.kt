package com.codefal.mystoryapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codefal.mystoryapp.databinding.FragmentStoryBinding
import com.codefal.mystoryapp.model.ListStoryItem


class MyItemAdapter: RecyclerView.Adapter<MyItemAdapter.ViewHolder>() {

    var onClick : ((ListStoryItem) -> Unit)? = null

    private val differCallback = object : DiffUtil.ItemCallback<ListStoryItem>(){
        override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
           return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

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
        val item = differ.currentList[position]
        holder.setItem(item)
        holder.setIsRecyclable(false)
        holder.binding.cardItem.setOnClickListener {
            onClick?.invoke(item)

        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(val binding: FragmentStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ListStoryItem){
            binding.apply {
                Glide.with(itemView).load(item.photoUrl).into(ivItemPhoto)
                tvItemName.text = item.name
            }
        }
    }

    fun setData(data: MutableList<ListStoryItem?>) = differ.submitList(data)

}