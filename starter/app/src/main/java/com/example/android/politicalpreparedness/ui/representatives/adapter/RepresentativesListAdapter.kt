package com.example.android.politicalpreparedness.ui.representatives.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.android.politicalpreparedness.models.Representative

class RepresentativesListAdapter : ListAdapter<Representative, RepresentativeViewHolder>(
    RepresentativeDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
