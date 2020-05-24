package com.example.android.politicalpreparedness.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionsBinding
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.ui.elections.adapter.ElectionsListAdapter
import com.husseinelfeky.githubpaging.common.paging.state.ResponseState

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionsBinding

    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentElectionsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        initAdapters()

        return binding.root
    }

    private fun initAdapters() {
        val onItemClicked = { election: Election ->
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election.name,
                    election
                )
            )
        }

        val upcomingElectionsAdapter = ElectionsListAdapter { election ->
            onItemClicked.invoke(election)
        }

        val savedElectionsAdapter = ElectionsListAdapter { election ->
            onItemClicked.invoke(election)
        }

        binding.apply {
            rvUpcomingElections.adapter = upcomingElectionsAdapter
            rvSavedElections.adapter = savedElectionsAdapter
        }

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            upcomingElectionsAdapter.submitList(it)
        })

        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            savedElectionsAdapter.submitList(it)
            binding.tvStateSavedElections.isVisible = it.isEmpty()
        })

        viewModel.stateUpcomingElections.observe(viewLifecycleOwner, Observer { responseState ->
            binding.apply {
                pbUpcomingElections.isVisible = responseState is ResponseState.Loading
                tvStateUpcomingElections.isVisible = responseState is ResponseState.Error

                if (responseState is ResponseState.Error) {
                    tvStateUpcomingElections.text = responseState.messageRes?.let { it ->
                        getString(it)
                    }
                }
            }
        })

        viewModel.stateSavedElections.observe(viewLifecycleOwner, Observer { responseState ->
            binding.pbSavedElections.isVisible = responseState is ResponseState.Loading
        })
    }
}
