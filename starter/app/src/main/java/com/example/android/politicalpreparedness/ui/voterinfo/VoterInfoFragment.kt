package com.example.android.politicalpreparedness.ui.voterinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.openUrl
import com.husseinelfeky.githubpaging.common.paging.state.ResponseState

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding

    private val args by navArgs<VoterInfoFragmentArgs>()

    private val viewModel by viewModels<VoterInfoViewModel> {
        VoterInfoViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.election = args.argElection

        initObservers()
        initClickListeners()

        viewModel.fetchDetails(args.argElection)

        return binding.root
    }

    private fun initObservers() {
        viewModel.voterInfo.observe(viewLifecycleOwner, Observer { voterInfo ->
            populateViews(voterInfo)
        })

        viewModel.stateVoterInfo.observe(viewLifecycleOwner, Observer { responseState ->
            binding.pbVoterInfo.isVisible = responseState is ResponseState.Loading
        })
    }

    private fun initClickListeners() {
        binding.btnFollowElection.setOnClickListener {
            viewModel.followOrUnfollowElection(args.argElection)
        }
    }

    private fun populateViews(voterInfo: VoterInfoResponse) {
        val administrationBody = voterInfo.state?.firstOrNull()?.electionAdministrationBody

        administrationBody?.let { administration ->
            binding.apply {
                var showElectionInfoHeader = false

                administration.votingLocationFinderUrl?.let { votingLocationsUrl ->
                    showElectionInfoHeader = true
                    stateLocations.setOnClickListener {
                        loadUrl(votingLocationsUrl)
                    }
                    stateLocations.isVisible = true
                }

                administration.ballotInfoUrl?.let { ballotInfoUrl ->
                    showElectionInfoHeader = true
                    stateBallot.setOnClickListener {
                        loadUrl(ballotInfoUrl)
                    }
                    stateBallot.isVisible = true
                }

                stateHeader.isVisible = showElectionInfoHeader

                administration.correspondenceAddress?.let { correspondenceAddress ->
                    address.text = correspondenceAddress.toFormattedString()
                    addressGroup.isVisible = true
                }
            }
        }
    }

    private fun loadUrl(url: String) {
        openUrl(requireContext(), url)
    }
}
