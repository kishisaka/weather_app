package com.ttyl.weatherapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices


class LocationProvider {
    fun getCurrentLocation(locationCallback: LocationCallback, activity: Activity) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                locationCallback.onSuccess(location)
            }
        } else {
            locationCallback.onFail()
        }
    }
}

interface LocationCallback {
    fun onSuccess(location: Location?)
    fun onFail()
}