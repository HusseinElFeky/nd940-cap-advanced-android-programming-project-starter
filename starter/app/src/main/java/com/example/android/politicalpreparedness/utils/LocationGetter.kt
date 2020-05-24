package com.example.android.politicalpreparedness.utils

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.android.politicalpreparedness.R
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LocationGetter(
    private val context: Context,
    private val locationListener: (Location) -> Unit
) {

    private val flpClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun isLocationEnabled(): Boolean {
        val locationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showDeviceLocationRequestDialog(onNoButtonClickedCallback: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.enable_device_location)
            .setMessage(R.string.msg_open_location_settings)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                ContextCompat.startActivity(context, Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), null)
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                onNoButtonClickedCallback.invoke()
            }
            .create()
            .show()
    }

    fun getLastLocation() {
        flpClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            if (location == null) {
                requestNewLocationData()
            } else {
                locationListener.invoke(location)
            }
        }
    }

    private fun requestNewLocationData() {
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationListener.invoke(locationResult.lastLocation)
            }
        }

        with(LocationRequest()) {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
            flpClient.requestLocationUpdates(this, locationCallback, Looper.myLooper())
        }
    }
}
