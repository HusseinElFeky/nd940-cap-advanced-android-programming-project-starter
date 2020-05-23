package com.example.android.politicalpreparedness.ui.representatives.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.android.politicalpreparedness.models.Representative

class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {

    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem.official == newItem.official
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }
}
