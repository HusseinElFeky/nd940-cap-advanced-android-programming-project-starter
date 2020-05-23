package com.example.android.politicalpreparedness.ui.elections.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.android.politicalpreparedness.models.Election

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {

    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}
