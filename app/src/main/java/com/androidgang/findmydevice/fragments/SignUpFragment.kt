package com.androidgang.findmydevice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidgang.findmydevice.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

     override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}