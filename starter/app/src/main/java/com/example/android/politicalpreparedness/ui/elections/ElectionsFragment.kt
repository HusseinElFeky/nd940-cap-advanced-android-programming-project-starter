package com.example.android.politicalpreparedness.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionsBinding

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionsBinding

    //TODO: Declare ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentElectionsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads
}
