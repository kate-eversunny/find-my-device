package com.androidgang.findmydevice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.androidgang.findmydevice.R
import com.androidgang.findmydevice.databinding.FragmentIntroBinding


class IntroFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.btnSignInIntro.setOnClickListener(this)
        binding.btnSignUpIntro.setOnClickListener(this)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_sign_in_intro -> {
                navController.navigate(R.id.action_introFragment_to_signInFragment)
            }
            R.id.btn_sign_up_intro -> {
                navController.navigate(R.id.action_introFragment_to_signUpFragment)
            }
        }
    }
}