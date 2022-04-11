package com.wiryadev.binar_movie.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.databinding.FragmentRegisterSuccessBinding

class RegisterSuccessFragment : Fragment() {

    private var _binding: FragmentRegisterSuccessBinding? = null
    private val binding get() = _binding

    private val args: RegisterSuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterSuccessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnLogin?.setOnClickListener {
            findNavController().navigate(
                RegisterSuccessFragmentDirections.actionRegisterSuccessFragmentToLoginFragment(
                    email = args.email
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}