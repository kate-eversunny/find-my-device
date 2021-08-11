package com.androidgang.findmydevice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidgang.findmydevice.R
import com.androidgang.findmydevice.adapters.BaseAdapterCallback
import com.androidgang.findmydevice.adapters.DevicesAdapter
import com.androidgang.findmydevice.adapters.decorators.SpacingItemDecoration
import com.androidgang.findmydevice.databinding.FragmentDevicesBinding
import com.androidgang.findmydevice.helpers.Constants
import com.androidgang.findmydevice.models.Smartphone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class DevicesFragment : Fragment(), View.OnClickListener {

	private var _binding: FragmentDevicesBinding? = null
	private val binding get() = _binding!!

	private lateinit var navController: NavController
	private lateinit var auth: FirebaseAuth

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		auth = Firebase.auth
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentDevicesBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		navController = Navigation.findNavController(view)
		binding.btnSignOut.setOnClickListener(this)
		val adapter = DevicesAdapter()
		binding.rvDevices.layoutManager =
			LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
		binding.rvDevices.adapter = adapter
		binding.rvDevices.addItemDecoration(SpacingItemDecoration(bottom = 20, top = 20))
		adapter.setList(Constants.phones)
		adapter.attachCallback(object : BaseAdapterCallback<Smartphone> {
			override fun onItemClick(model: Smartphone, view: View) {
				val bundle = bundleOf(Constants.TAG_BUNDLE_DEVICE_ID to model.id)
				navController.navigate(R.id.action_devicesFragment_to_mapsFragment, bundle)
			}
		})
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun onClick(v: View?) {
		when (v!!.id) {
			R.id.btn_sign_out -> {
				auth.signOut()
				navController.navigate(R.id.action_devicesFragment_to_introFragment)
			}
		}
	}
}