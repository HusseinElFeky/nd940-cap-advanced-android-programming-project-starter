package com.example.android.politicalpreparedness.ui.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding

class LaunchFragment : Fragment() {

    private lateinit var binding: FragmentLaunchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchBinding.inflate(inflater)
        binding.lifecycleOwner = this

        initClickListeners()

        return binding.root
    }

    private fun initClickListeners() {
        with(binding) {
            btnElections.setOnClickListener {
                findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
            }

            btnRepresentatives.setOnClickListener {
                findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativesFragment())
            }
        }
    }
}
