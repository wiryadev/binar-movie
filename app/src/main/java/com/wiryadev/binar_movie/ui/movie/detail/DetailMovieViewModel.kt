package com.wiryadev.binar_movie.ui.movie.detail

import androidx.lifecycle.*
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.movie.MovieRepository
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
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

    private val _movie: MutableStateFlow<DetailMovieUiState> =
        MutableStateFlow(DetailMovieUiState())
    val movie: LiveData<DetailMovieUiState> get() = _movie.asLiveData()

    fun getDetail(movieId: Int) {
        _movie.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            movieRepository.getMovieDetail(movieId = movieId).collectLatest { result ->
                when (result) {
                    is Result.Success -> _movie.update {
                        it.copy(
                            isLoading = false,
                            movie = result.data
                        )
                    }
                    is Result.Error -> _movie.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message
                        )
                    }
                }
            }
        }
    }
}

data class DetailMovieUiState(
    val isLoading: Boolean = false,
    val movie: DetailMovieResponse? = null,
    val errorMessage: String? = null,
)