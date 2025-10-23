package com.wallpaper.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.wallpaper.app.R
import com.wallpaper.app.data.model.Photo
import com.wallpaper.app.databinding.ItemWallpaperBinding

class WallpaperAdapter(
    private val onItemClick: (Photo) -> Unit
) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {

    private val wallpapers = mutableListOf<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val binding = ItemWallpaperBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WallpaperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        holder.bind(wallpapers[position])
    }

    override fun getItemCount(): Int = wallpapers.size

    fun submitList(newWallpapers: List<Photo>) {
        val diffCallback = WallpaperDiffCallback(wallpapers, newWallpapers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        
        wallpapers.clear()
        wallpapers.addAll(newWallpapers)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class WallpaperViewHolder(
        private val binding: ItemWallpaperBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            Glide.with(binding.imageView.context)
                .load(photo.src.medium)
                .centerCrop()
                .placeholder(R.color.placeholder_color)
                .error(R.color.error_color)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)

            binding.photographerTextView.text = photo.photographer

            binding.root.setOnClickListener {
                onItemClick(photo)
            }
        }
    }

    private class WallpaperDiffCallback(
        private val oldList: List<Photo>,
        private val newList: List<Photo>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
