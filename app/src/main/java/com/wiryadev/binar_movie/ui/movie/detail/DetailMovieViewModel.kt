package com.wiryadev.binar_movie.ui.movie.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.repositories.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailMovieUiState> =
        MutableStateFlow(DetailMovieUiState())
    val uiState: LiveData<DetailMovieUiState> get() = _uiState.asLiveData()

    fun getDetail(movieId: Int) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            movieRepository.getMovieDetail(movieId = movieId).collectLatest { result ->
                when (result) {
                    is Result.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            movie = result.data
                        )
                    }
                    is Result.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message
                        )
                    }
                }
            }
        }
    }

    fun checkIsFavorite(id: Int) = viewModelScope.launch {
        movieRepository.checkFavoriteMovie(id = id).collect { movie ->
            _uiState.update {
                it.copy(
                    isFavorite = movie > 0
                )
            }
        }
    }

    fun addFavoriteMovie(movie: DetailMovieResponse) = viewModelScope.launch {
        movieRepository.addFavoriteMovie(
            movie = MovieEntity(
                movieId = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
            )
        )
    }

    fun deleteFavoriteMovie(movie: DetailMovieResponse) = viewModelScope.launch {
        movieRepository.deleteFavoriteMovie(
            movie = MovieEntity(
                movieId = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
            )
        )
    }

}

data class DetailMovieUiState(
    val isLoading: Boolean = false,
    val movie: DetailMovieResponse? = null,
    val isFavorite: Boolean = false,
    val errorMessage: String? = null,
)