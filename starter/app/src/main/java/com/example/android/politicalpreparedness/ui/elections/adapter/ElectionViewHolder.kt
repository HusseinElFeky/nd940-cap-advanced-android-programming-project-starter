package com.example.android.politicalpreparedness.ui.elections.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.models.Election

class ElectionViewHolder(
    private val binding: ItemElectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(election: Election, onItemClicked: (Election) -> Unit) {
        binding.election = election
        itemView.setOnClickListener {
            onItemClicked.invoke(election)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ElectionViewHolder {
            val binding = ItemElectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ElectionViewHolder(binding)
        }
    }
}
