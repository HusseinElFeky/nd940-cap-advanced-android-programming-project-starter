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
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState

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
                    election.id,
                    election.division
                )
            )
        }

        val upcomingElectionsAdapter = ElectionsListAdapter { election ->
            onItemClicked.invoke(election)
        }

        val savedElectionsAdapter = ElectionsListAdapter { election ->
            onItemClicked.invoke(election)
        }

        with(binding) {
            rvUpcomingElections.adapter = upcomingElectionsAdapter
            rvSavedElections.adapter = savedElectionsAdapter
        }

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            upcomingElectionsAdapter.submitList(it)
        })

        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            savedElectionsAdapter.submitList(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer { networkState ->
            with(binding) {
                progressBar.isVisible = networkState is NetworkState.Loading
                tvError.isVisible = networkState is NetworkState.Error

                if (networkState is NetworkState.Error) {
                    tvError.text = networkState.messageRes?.let { it ->
                        getString(it)
                    }
                }
            }
        })
    }
}
