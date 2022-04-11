package com.wiryadev.binar_movie.ui.movie.list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wiryadev.binar_movie.data.repositories.movie.MovieRepository
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val movies: LiveData<PagingData<MovieDto>> =
        movieRepository.discoverMovies().cachedIn(viewModelScope)

}