package com.martinz.myreminder.presentation.main_screen.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martinz.myreminder.databinding.LocationItemBinding
import com.martinz.myreminder.domain.model.Reminder

class LocationAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Reminder, LocationAdapter.ViewHolder>(LocationComparator()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LocationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class ViewHolder(private val binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mReminder: Reminder) {
            binding.apply {
                binding.deleteBtn.setOnClickListener {
                    onClickListener.onClick(mReminder)
                }
                location = mReminder
            }
        }
    }

    class LocationComparator : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
            oldItem == newItem

    }




}