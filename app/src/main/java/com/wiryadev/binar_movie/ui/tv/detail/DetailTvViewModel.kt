package com.wiryadev.binar_movie.ui.tv.detail

import androidx.lifecycle.*
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.repositories.tv.TvRepository
import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTvViewModel @Inject constructor(
    private val tvRepository: TvRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailTvUiState> =
        MutableStateFlow(DetailTvUiState())
    val uiState: LiveData<DetailTvUiState> get() = _uiState.asLiveData()

    fun getDetail(tvId: Int) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            tvRepository.getTvShowDetail(tvId = tvId).collectLatest { result ->
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

    fun checkIsFavorite(id: Int) = tvRepository.checkFavoriteTv(id = id).asLiveData()

    fun addFavoriteMovie(tv: DetailTvResponse) = viewModelScope.launch {
        tvRepository.addFavoriteTv(
            tv = TvEntity(
                tvId = tv.id,
                title = tv.name,
                posterPath = tv.posterPath,
            )
        )
    }

    fun deleteFavoriteMovie(tv: DetailTvResponse) = viewModelScope.launch {
        tvRepository.deleteFavoriteTv(
            tv = TvEntity(
                tvId = tv.id,
                title = tv.name,
                posterPath = tv.posterPath,
            )
        )
    }
}

data class DetailTvUiState(
    val isLoading: Boolean = false,
    val movie: DetailTvResponse? = null,
    val errorMessage: String? = null,
)