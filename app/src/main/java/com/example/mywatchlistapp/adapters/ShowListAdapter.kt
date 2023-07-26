package com.example.mywatchlistapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mywatchlistapp.data.Show
import com.example.mywatchlistapp.databinding.ShowItemBinding

class ShowListAdapter(private val onItemClicked: (Show) -> Unit) :
    ListAdapter<Show, ShowListAdapter.ShowViewHolder>(DiffCallback) {

    inner class ShowViewHolder(private val binding: ShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show: Show) {
            binding.apply {
                title.text = show.title
                rating.text = show.rating.toString()
                timestamp.text = show.timestamp.substring(0, show.timestamp.length - 3)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(
            ShowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener { onItemClicked(current) }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Show>() {
            override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
                // Data class already has a built-in compare method to compare all properties
                return oldItem == newItem
            }
        }
    }
}