package com.wiryadev.binar_movie.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.binar_movie.data.preference.AuthModel
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: LiveData<LoginUiState> get() = _uiState.asLiveData()

    fun login(
        email: String,
        password: String,
    ) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                userRepository.login(
                    email = email,
                    password = password,
                ).collectLatest { user ->
                    userRepository.saveUserSession(
                        AuthModel(
                            username = user.username,
                            email = user.email,
                        )
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "User Not Found",
                    )
                }
            }
        }
    }

}

data class LoginUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)