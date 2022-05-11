package com.wiryadev.binar_movie.ui.movie.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.repositories.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieFavoriteViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FavoriteMovieUiState> =
        MutableStateFlow(FavoriteMovieUiState())
    val uiState: LiveData<FavoriteMovieUiState> get() = _uiState.asLiveData()

    init {
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            try {
                movieRepository.getFavoriteMovies().collectLatest { movies ->
                    _uiState.update {
                        it.copy(isLoading = false, movies = movies)
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

data class FavoriteMovieUiState(
    val isLoading: Boolean = false,
    val movies: List<MovieEntity> = emptyList(),
    val errorMessage: String? = null,
)