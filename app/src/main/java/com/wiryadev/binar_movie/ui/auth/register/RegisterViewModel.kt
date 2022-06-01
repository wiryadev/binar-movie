package com.wiryadev.binar_movie.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val queryChannel = MutableStateFlow("")

    private val _uiState: MutableStateFlow<RegisterUiState> = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> get() = _uiState.asStateFlow()

    val checkUserExist = queryChannel
        .debounce(200)
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            userRepository.checkUserExist(it)
        }
        .catch {
            _uiState.value = RegisterUiState(
                errorMessage = "Something Went Wrong"
            )
        }
        .asLiveData(Dispatchers.IO)

    fun register(user: UserEntity) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            try {
                userRepository.register(user = user)
                _uiState.update {
                    it.copy(isLoading = false, isSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)