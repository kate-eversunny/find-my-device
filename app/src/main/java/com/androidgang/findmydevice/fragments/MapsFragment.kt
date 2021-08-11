package com.androidgang.findmydevice.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.androidgang.findmydevice.R
import com.androidgang.findmydevice.databinding.FragmentMapsBinding
import com.androidgang.findmydevice.helpers.Constants
import com.androidgang.findmydevice.models.LocationTracker
import com.androidgang.findmydevice.models.Smartphone
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates.notNull


class MapsFragment : Fragment() {

	private var _binding: FragmentMapsBinding? = null
	private val binding get() = _binding!!

	private lateinit var navController: NavController
	private var idDevice by notNull<Int>()
	private lateinit var device: Smartphone
	private lateinit var auth: FirebaseAuth
	private lateinit var locationTracker: LocationTracker

	private val callback = OnMapReadyCallback { googleMap ->
		val devicePlace = LatLng(device.latitude, device.longitude)
		googleMap.uiSettings.isZoomControlsEnabled = true
		googleMap.uiSettings.isCompassEnabled = true
		googleMap.uiSettings.isMyLocationButtonEnabled = true
		googleMap.uiSettings.setAllGesturesEnabled(true)
		googleMap.isTrafficEnabled = true
		val cameraPosition = CameraPosition.Builder().target(devicePlace).zoom(12f).build()
		googleMap.addMarker(MarkerOptions().position(devicePlace).title("Here is your device"))
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

		val circleOptions = CircleOptions()
			.center(devicePlace)
			.radius(1000.0)
			.fillColor(Color.parseColor("#79C15656"))
			.strokeColor(Color.BLUE)
			.strokeWidth(5f)

		googleMap.addCircle(circleOptions)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		locationTracker = LocationTracker(this.activity as Context)
		locationTracker.startLocationUpdates()
		updateGps()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		auth = Firebase.auth
		idDevice = requireArguments().getInt(Constants.TAG_BUNDLE_DEVICE_ID)
		device = Constants.phones.find { it.id == idDevice }!!
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

		_binding = FragmentMapsBinding.inflate(inflater, container, false)
		return binding.root
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		navController = Navigation.findNavController(view)

		val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
		mapFragment?.getMapAsync(callback)
		binding.itemDevice.tvItemDeviceImei.text = "IMEI: ${device.imei}"
		binding.itemDevice.tvItemDeviceName.text = device.name
		binding.itemDevice.tvItemDeviceLastSeen.text =
			"Last seen: ${Constants.sdf.format(Date(device.lastSeen))}"
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun onResume() {
		super.onResume()
		locationTracker.startLocationUpdates()
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		when (requestCode) {
			Constants.PERMISSIONS_FINE_LOCATION -> {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					updateGps()
				} else {
					Toast.makeText(
						this.context,
						"This app needs a permission to be granted to work properly",
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}

	private fun updateGps() {
		if (ActivityCompat.checkSelfPermission(
				this.requireContext(),
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			val scope = CoroutineScope(Dispatchers.Main)
			scope.launch {
				launch(Dispatchers.Main) {
					locationTracker.updateGps(auth.currentUser!!)
				}.join()
			}
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				val permissions: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
				requestPermissions(permissions, Constants.PERMISSIONS_FINE_LOCATION)
			}
		}
	}
}