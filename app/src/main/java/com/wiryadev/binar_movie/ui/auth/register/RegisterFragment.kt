package com.wiryadev.binar_movie.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.ui.components.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@ExperimentalMaterial3Api
@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private var emailToBeSent = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                val uiState by viewModel.uiState.collectAsState()

                if (uiState.isSuccess) {
                    navigateToRegisterSuccessScreen()
                }

                // decouple snackbar host state from scaffold state for demo purposes
                // this state, channel and flow is for demo purposes to demonstrate business logic layer
                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                // we allow only one snackbar to be in the queue here, hence conflated
                val channel = remember { Channel<String>(Channel.CONFLATED) }
                LaunchedEffect(channel) {
                    channel.receiveAsFlow().collect { message ->
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = "OK"
                        )
                    }
                }

                uiState.errorMessage?.let {
                    channel.trySend(it)
                }

                Mdc3Theme {
                    Scaffold(
                        modifier = Modifier.padding(16.dp),
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        }
                    ) { contentPadding ->
                        LazyColumn(
                            contentPadding = contentPadding,
                        ) {
                            item {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = stringResource(id = R.string.register),
                                        style = MaterialTheme.typography.displayMedium
                                    )
                                    Image(
                                        painter = painterResource(id = R.drawable.banner_binar),
                                        contentDescription = stringResource(id = R.string.banner),
                                        contentScale = ContentScale.FillHeight,
                                        modifier = Modifier.height(128.dp),
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                            item {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    val passwordFocusRequest = remember { FocusRequester() }
                                    val confirmationPasswordFocusRequest = remember {
                                        FocusRequester()
                                    }

                                    val usernameState = remember { TextFieldState() }
                                    UsernameTextField(usernameState)

                                    Spacer(modifier = Modifier.height(16.dp))
                                    val emailState = remember { EmailState() }
                                    EmailTextField(
                                        emailState = emailState,
                                        onImeAction = { passwordFocusRequest.requestFocus() }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
                                    val passwordState = remember { PasswordState() }
                                    PasswordTextField(
                                        label = stringResource(id = R.string.password),
                                        passwordState = passwordState,
                                        imeAction = ImeAction.Next,
                                        onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
                                        modifier = Modifier.focusRequester(passwordFocusRequest)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    val confirmPasswordState = remember {
                                        ConfirmPasswordState(passwordState = passwordState)
                                    }
                                    PasswordTextField(
                                        label = stringResource(id = R.string.confirm_password),
                                        passwordState = confirmPasswordState,
                                        onImeAction = {
                                            viewModel.register(
                                                UserEntity(
                                                    username = usernameState.text,
                                                    email = emailState.text,
                                                    password = passwordState.text,
                                                )
                                            )
                                        },
                                        modifier = Modifier.focusRequester(
                                            confirmationPasswordFocusRequest
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(
                                        onClick = {
                                            emailToBeSent = emailState.text

                                            viewModel.register(
                                                UserEntity(
                                                    username = usernameState.text,
                                                    email = emailState.text,
                                                    password = passwordState.text,
                                                )
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = emailState.isValid
                                                && passwordState.isValid
                                                && confirmPasswordState.isValid
                                                && !uiState.isLoading
                                    ) {
                                        if (uiState.isLoading) {
                                            LinearProgressIndicator()
                                        } else {
                                            Text(text = stringResource(id = R.string.register))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToRegisterSuccessScreen() {
        findNavController().navigate(
            RegisterFragmentDirections.actionRegisterFragmentToRegisterSuccessFragment(
                email = emailToBeSent
            )
        )
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initListener()
//
//        binding.etEmail.addTextChangedListener(
//            onTextChanged = { text, _, _, _ ->
//                text?.let {
//                    viewModel.queryChannel.value = it.toString()
//                }
//            }
//        )
//
//        viewModel.checkUserExist.observe(viewLifecycleOwner) {
//            if (it > 0) {
//                binding.tilEmail.error = "Email sudah ada"
//            }
//        }
//
//        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
//            uiState.errorMessage?.let {
//                binding.root.showSnackbar(it)
//            }
//
//            binding.btnRegister.isVisible = !uiState.isLoading
//
//            if (uiState.isSuccess) {
//                findNavController().navigate(
//                    RegisterFragmentDirections.actionRegisterFragmentToRegisterSuccessFragment(
//                        email = emailToBeSent
//                    )
//                )
//            }
//        }
//
//        binding.btnRegister.setOnClickListener {
//            checkInputAndRegister()
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun initListener() {
//        with(binding) {
//            etUsername.addErrorListener("Username")
//            etEmail.addErrorListener("Email") {
//                Patterns.EMAIL_ADDRESS.matcher(it).matches()
//            }
//            etPassword.addErrorListener("Password")
//            etConfirmPassword.addErrorListener("Confirm Password") {
//                it == etPassword.text.toString()
//            }
//        }
//    }
//
//    private fun checkInputAndRegister() {
//        with(binding) {
//            val username = etUsername.text.toString().trim()
//            val email = etEmail.text.toString().trim()
//            val password = etPassword.text.toString().trim()
//
//            when {
//                tilUsername.error != null -> {
//                    root.showSnackbar("Username tidak boleh kosong")
//                }
//                tilEmail.error != null -> {
//                    root.showSnackbar("Email tidak boleh kosong")
//                }
//                tilPassword.error != null -> {
//                    root.showSnackbar("Password tidak boleh kosong")
//                }
//                tilConfirmPassword.error != null -> {
//                    root.showSnackbar("Pastikan konfirmasi password sesuai")
//                }
//                else -> {
//                    emailToBeSent = email
//                    viewModel.register(
//                        user = UserEntity(
//                            email = email,
//                            username = username,
//                            password = password,
//                        )
//                    )
//                }
//            }
//        }
//    }

}