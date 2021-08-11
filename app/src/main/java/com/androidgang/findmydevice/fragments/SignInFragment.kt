package com.androidgang.findmydevice.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.androidgang.findmydevice.R
import com.androidgang.findmydevice.databinding.FragmentSignInBinding
import com.androidgang.findmydevice.helpers.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SignInFragment : Fragment(), View.OnClickListener {

	private var _binding: FragmentSignInBinding? = null
	private val binding get() = _binding!!
	private val database = Firebase.database(Constants.FIREBASE_DATABASE_URL).reference

	private lateinit var navController: NavController
	private lateinit var auth: FirebaseAuth
	private lateinit var inputManager: InputMethodManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		auth = Firebase.auth
		inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

		_binding = FragmentSignInBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		navController = Navigation.findNavController(view)
		binding.btnSignIn.setOnClickListener(this)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun onClick(v: View?) {
		when (v!!.id) {
			R.id.btn_sign_in -> {
				inputManager.hideSoftInputFromWindow(
					view?.windowToken,
					InputMethodManager.HIDE_NOT_ALWAYS
				)
				if (isInputValid()) {
					signInAccount()
				}
			}
		}
	}

	private fun isInputValid(): Boolean {
		if (binding.etEmailSignIn.text.toString().isEmpty()) {
			Toast.makeText(
				this.context, ("Please enter your email address"),
				Toast.LENGTH_SHORT
			).show()
			return false
		} else if (binding.etPasswordSignIn.text.toString().isEmpty()) {
			Toast.makeText(
				this.context, ("Please enter a password"),
				Toast.LENGTH_SHORT
			).show()
			return false
		}
		return true
	}

	private fun signInAccount() {
		auth.signInWithEmailAndPassword(
			binding.etEmailSignIn.text.toString(),
			binding.etPasswordSignIn.text.toString()
		)
			.addOnCompleteListener(this.activity as Activity) { task ->
				if (task.isSuccessful) {
					// Sign in success, update UI with the signed-in user's information
					Log.d(tag, "signInWithEmail:success")
					var flag = false
					database.child("Users").child(auth.currentUser!!.uid).child("Devices")
						.addValueEventListener(
							object : ValueEventListener {
								override fun onDataChange(snapshot: DataSnapshot) {
									if (snapshot.value != null) {
										val td = snapshot.value as HashMap<*, *>
										for (key in td.keys) {
											if (key == android.os.Build.MODEL) {
												flag = true
												break
											}
										}
									}
									if (!flag) {
										database.child("Users").child(auth.currentUser!!.uid)
											.child("Devices").child(android.os.Build.MODEL)
									}
								}

								override fun onCancelled(error: DatabaseError) {
									TODO("Not yet implemented")
								}
							}
						)
					navController.navigate(R.id.action_signInFragment_to_devicesFragment)
				} else {
					// If sign in fails, display a message to the user.
					Log.w(tag, "signInWithEmail:failure", task.exception)
					Toast.makeText(
						context, "Authentication failed. Please check email and password",
						Toast.LENGTH_SHORT
					).show()
				}
			}

	}


}