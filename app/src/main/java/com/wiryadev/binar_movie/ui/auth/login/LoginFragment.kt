package com.wiryadev.binar_movie.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.databinding.FragmentLoginBinding
import com.wiryadev.binar_movie.ui.MainActivity
import com.wiryadev.binar_movie.ui.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val args: LoginFragmentArgs by navArgs()
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.email?.let {
            binding.etEmail.setText(it)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            uiState.errorMessage?.let {
                binding.root.showSnackbar(it)
            }

            binding.btnLogin.isVisible = !uiState.isLoading

            if (uiState.isLoggedIn) {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        binding.btnLogin.setOnClickListener {
            checkInputAndRegister()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkInputAndRegister() {
        with(binding) {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                tilEmail.error != null -> {
                    root.showSnackbar("Email tidak boleh kosong")
                }
                tilPassword.error != null -> {
                    root.showSnackbar("Password tidak boleh kosong")
                }
                else -> {
                    viewModel.login(
                        email = email,
                        password = password,
                    )
                }
            }
        }
    }

}