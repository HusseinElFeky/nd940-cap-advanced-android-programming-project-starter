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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativesBinding
import com.example.android.politicalpreparedness.models.Address
import com.example.android.politicalpreparedness.utils.LocationGetter
import com.example.android.politicalpreparedness.utils.PermissionsHelper
import com.example.android.politicalpreparedness.utils.PermissionsHelper.PERMISSION_FINE_LOCATION
import com.example.android.politicalpreparedness.utils.PermissionsHelper.REQUEST_FINE_LOCATION_PERMISSION
import com.google.android.material.snackbar.Snackbar
import java.util.*

class RepresentativesFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativesBinding

    private val viewModel by viewModels<RepresentativesViewModel> {
        RepresentativesViewModelFactory()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRepresentativesBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_FINE_LOCATION_PERMISSION -> {
                if (grantResults.first() != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(
                        binding.root,
                        "Location permission is needed to fetch your current location.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    return
                }
                getLocationIfPossible()
            }
        }
    }

    private fun checkLocationPermissions() {
        return if (PermissionsHelper.isPermissionGranted(requireContext(), PERMISSION_FINE_LOCATION)) {
            getLocationIfPossible()
        } else {
            PermissionsHelper.requestPermission(this, PERMISSION_FINE_LOCATION, REQUEST_FINE_LOCATION_PERMISSION)
        }
    }

    private fun getLocationIfPossible() {
        val locationGetter = LocationGetter(requireContext()) { location ->
            val address = geoCodeLocation(location)
            //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
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
}
