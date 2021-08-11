package com.androidgang.findmydevice.models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.androidgang.findmydevice.helpers.Constants.FIREBASE_DATABASE_URL
import com.androidgang.findmydevice.helpers.Constants.LOCATION_REQUEST_FASTEST_INTERVAL
import com.androidgang.findmydevice.helpers.Constants.LOCATION_REQUEST_INTERVAL
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LocationTracker(val context: Context) {
	private var fusedLocationProviderClient: FusedLocationProviderClient =
		LocationServices.getFusedLocationProviderClient(context)
	private var locationRequest: LocationRequest = LocationRequest.create()
	private var locationCallback: LocationCallback
	private val database = Firebase.database(FIREBASE_DATABASE_URL).reference

	var location: Location? = null

	init {
		locationRequest.interval = LOCATION_REQUEST_INTERVAL
		locationRequest.fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
		locationRequest.priority = PRIORITY_BALANCED_POWER_ACCURACY

		locationCallback = object : LocationCallback() {
			override fun onLocationResult(locationResult: LocationResult?) {
				locationResult ?: return
				for (l in locationResult.locations) {
					location = locationResult.locations[0]
				}
			}
		}
	}

	fun startLocationUpdates() {
		if (ActivityCompat.checkSelfPermission(
				context,
				Manifest.permission.ACCESS_FINE_LOCATION
			) != PackageManager.PERMISSION_GRANTED
		) {
			fusedLocationProviderClient.requestLocationUpdates(
				locationRequest,
				locationCallback,
				Looper.getMainLooper()
			)
		}
	}

	fun updateGps(currentUser: com.google.firebase.auth.FirebaseUser) {
		if (ActivityCompat.checkSelfPermission(
				context,
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			fusedLocationProviderClient.lastLocation.addOnSuccessListener {
				if (it == null) {
					fusedLocationProviderClient.requestLocationUpdates(
						locationRequest,
						locationCallback,
						Looper.getMainLooper()
					)
				}
			}.addOnCompleteListener {
				if (it.isSuccessful) {
					database.child("Users").child(currentUser.uid).child("Devices").child(android.os.Build.MODEL).child("Location").setValue(it.result)
				}
			}
		}
	}

	fun stopLocationUpdates() {
		fusedLocationProviderClient.removeLocationUpdates(locationCallback)
	}
}