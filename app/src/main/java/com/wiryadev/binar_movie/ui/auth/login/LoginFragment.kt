package com.wiryadev.binar_movie.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.ui.MainActivity
import com.wiryadev.binar_movie.ui.components.EmailState
import com.wiryadev.binar_movie.ui.components.EmailTextField
import com.wiryadev.binar_movie.ui.components.PasswordState
import com.wiryadev.binar_movie.ui.components.PasswordTextField
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val args: LoginFragmentArgs by navArgs()
    private val viewModel: LoginViewModel by viewModels()

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

                if (uiState.isLoggedIn) {
                    navigateToHomeScreen()
                }

                Mdc3Theme {
                    Scaffold(
                        modifier = Modifier.padding(16.dp)
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
                                        text = stringResource(id = R.string.login),
                                        style = MaterialTheme.typography.headlineMedium,
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
                                    val focusRequester = remember { FocusRequester() }
                                    val emailState = remember {
                                        EmailState(args.email ?: "")
                                    }
                                    EmailTextField(
                                        emailState = emailState,
                                        onImeAction = { focusRequester.requestFocus() }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    val passwordState = remember { PasswordState() }
                                    PasswordTextField(
                                        label = stringResource(id = R.string.password),
                                        passwordState = passwordState,
                                        modifier = Modifier.focusRequester(focusRequester),
                                        onImeAction = {
                                            viewModel.login(
                                                emailState.text,
                                                passwordState.text
                                            )
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            viewModel.login(
                                                emailState.text,
                                                passwordState.text
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                top = 16.dp,
                                                bottom = 8.dp,
                                            ),
                                        enabled = emailState.isValid
                                                && passwordState.isValid
                                                && !uiState.isLoading
                                    ) {
                                        if (uiState.isLoading) {
                                            LinearProgressIndicator()
                                        } else {
                                            Text(
                                                text = stringResource(id = R.string.login)
                                            )
                                        }
                                    }
                                }
                            }
                            item {
                                TextButton(
                                    onClick = ::navigateToRegisterScreen,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = stringResource(id = R.string.dont_have_account))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToRegisterScreen() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        )
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}