package com.wiryadev.binar_movie.ui.tv.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.repositories.movie.MovieRepository
import com.wiryadev.binar_movie.data.repositories.tv.TvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvFavoriteViewModel @Inject constructor(
    private val tvRepository: TvRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FavoriteTvUiState> =
        MutableStateFlow(FavoriteTvUiState())
    val uiState: LiveData<FavoriteTvUiState> get() = _uiState.asLiveData()

    init {
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            try {
                tvRepository.getFavoriteTvs().collectLatest { tvShows ->
                    _uiState.update {
                        it.copy(isLoading = false, tvShows = tvShows)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

}

data class FavoriteTvUiState(
    val isLoading: Boolean = false,
    val tvShows: List<TvEntity> = emptyList(),
    val errorMessage: String? = null,
)