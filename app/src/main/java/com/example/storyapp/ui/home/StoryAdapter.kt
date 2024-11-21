package com.example.storyapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.R
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ItemStoriesBinding

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class StoryViewHolder(private val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvSender.text = story.name
            binding.tvDesc.text = story.description
            Glide.with(binding.root.context).load(story.photoUrl)
                .apply(RequestOptions.placeholderOf(
                    R.drawable.outline_image_24).error(R.drawable.rounded_broken_image_24))
                .circleCrop()
                .into(binding.imgStory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(story)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}