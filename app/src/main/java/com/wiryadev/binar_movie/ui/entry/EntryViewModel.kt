package com.wiryadev.binar_movie.ui.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun getUser() {
        viewModelScope.launch {
            userRepository.getUserSession().collectLatest { user ->
                _isLoggedIn.value = user.email.isNotBlank()
            }
        }
    }
}