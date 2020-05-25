package com.example.android.politicalpreparedness.ui.representatives

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativesBinding
import com.example.android.politicalpreparedness.models.Address
import com.example.android.politicalpreparedness.ui.representatives.adapter.RepresentativesListAdapter
import com.example.android.politicalpreparedness.utils.LocationGetter
import com.example.android.politicalpreparedness.utils.PermissionsHelper
import com.example.android.politicalpreparedness.utils.PermissionsHelper.PERMISSIONS_LOCATION
import com.example.android.politicalpreparedness.utils.PermissionsHelper.REQUEST_LOCATION_PERMISSIONS
import com.google.android.material.snackbar.Snackbar
import com.husseinelfeky.githubpaging.common.paging.state.ResponseState
import java.util.*

class RepresentativesFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativesBinding

    private val viewModel by viewModels<RepresentativesViewModel> {
        RepresentativesViewModelFactory()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRepresentativesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initAdapter()
        initClickListeners()

        return binding.root
    }

    private fun initAdapter() {
        val representativesAdapter = RepresentativesListAdapter()
        binding.rvRepresentatives.adapter = representativesAdapter

        viewModel.representatives.observe(viewLifecycleOwner, Observer {
            representativesAdapter.submitList(it)
            if (it.isEmpty()) {
                binding.motionLayout.getTransition(R.id.transition_form).setEnable(false)
                binding.tvStateRepresentatives.apply {
                    text = getString(R.string.no_representatives_fetched)
                    isVisible = true
                }
            } else {
                binding.motionLayout.getTransition(R.id.transition_form).setEnable(true)
                if (viewModel.stateRepresentatives.value !is ResponseState.Error) {
                    binding.tvStateRepresentatives.isVisible = false
                }
            }
        })

        viewModel.stateRepresentatives.observe(viewLifecycleOwner, Observer { responseState ->
            binding.apply {
                pbRepresentatives.isVisible = responseState is ResponseState.Loading
                tvStateRepresentatives.isVisible = responseState is ResponseState.Error
                rvRepresentatives.isVisible = responseState is ResponseState.Loaded

                if (responseState is ResponseState.Error) {
                    tvStateRepresentatives.text = responseState.messageRes?.let { it ->
                        getString(it)
                    }
                }
            }
        })
    }

    private fun initClickListeners() {
        binding.apply {
            btnSearch.setOnClickListener {
                hideKeyboard()
                this@RepresentativesFragment.viewModel.getRepresentatives(
                    Address(
                        line1 = addressLine1.text.toString(),
                        line2 = addressLine2.text.toString(),
                        city = city.text.toString(),
                        state = state.selectedItem.toString(),
                        zip = zip.text.toString()
                    )
                )
            }

            btnLocation.setOnClickListener {
                hideKeyboard()
                checkLocationPermissions()
            }
        }
    }

    private fun checkLocationPermissions() {
        return if (PermissionsHelper.arePermissionsGranted(requireContext(), PERMISSIONS_LOCATION)) {
            getLocationIfPossible()
        } else {
            PermissionsHelper.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION_PERMISSIONS)
        }
    }

    private fun getLocationIfPossible() {
        val locationGetter = LocationGetter(requireContext()) { location ->
            val address = geoCodeLocation(location)
            viewModel.updateAddress(address)
        }

        if (locationGetter.isLocationEnabled()) {
            locationGetter.getLastLocation()
        } else {
            locationGetter.showDeviceLocationRequestDialog {
                Snackbar.make(
                    binding.root,
                    R.string.error_device_location_disabled,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSIONS -> {
                grantResults.forEach { grantResult ->
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Snackbar.make(
                            binding.root,
                            R.string.error_location_permission_required,
                            Snackbar.LENGTH_LONG
                        ).show()
                        return
                    }
                }
                getLocationIfPossible()
            }
        }
    }
}
