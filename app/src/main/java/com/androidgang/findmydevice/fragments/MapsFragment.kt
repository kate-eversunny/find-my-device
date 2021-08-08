package com.androidgang.findmydevice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.androidgang.findmydevice.R
import com.androidgang.findmydevice.databinding.FragmentMapsBinding
import com.androidgang.findmydevice.helpers.Constants
import com.androidgang.findmydevice.models.Smartphone
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.properties.Delegates.notNull

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private var idDevice by notNull<Int>()
    private lateinit var device: Smartphone


    private val callback = OnMapReadyCallback { googleMap ->
        val devicePlace = LatLng(device.latitude, device.longitude)
        googleMap.addMarker(MarkerOptions().position(devicePlace).title("Here is your device"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(devicePlace))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.itemDevice.tvItemDeviceLastSeen.text = "Last seen: ${Constants.sdf.format(Date(device.lastSeen))}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}