package com.wiryadev.binar_movie.ui.profile

import androidx.lifecycle.*
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.data.preference.AuthModel
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userSession: MutableLiveData<AuthModel> = MutableLiveData()
    val userSession: LiveData<AuthModel> get() = _userSession

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState())
    val uiState: LiveData<ProfileUiState> get() = _uiState.asLiveData()

    fun checkUserSession() {
        viewModelScope.launch {
            userRepository.getUserSession().collect {
                _userSession.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.deleteUserSession()
        }
    }

    fun getUser(email: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            userRepository.getUser(email = email).collectLatest { user ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                    )
                }
            }
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            val result = userRepository.updateUser(user = user)
            _uiState.update {
                when (result) {
                    is Result.Success -> it.copy(
                        isLoading = false,
                        result = result.data,
                    )
                    is Result.Error -> it.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )
                }
            }
        }
    }

    fun updateProfilePic(picture: String) {
        _uiState.update {
            it.copy(picture = picture)
        }
    }

    fun resetUpdateStatus() {
        _uiState.update {
            it.copy(result = 0)
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserEntity? = null,
    val picture: String? = null,
    val result: Int = 0,
    val errorMessage: String? = null,
)