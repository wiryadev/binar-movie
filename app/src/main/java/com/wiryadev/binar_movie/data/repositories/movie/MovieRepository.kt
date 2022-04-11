package com.wiryadev.binar_movie.data.repositories.movie

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun discoverMovies(): LiveData<PagingData<MovieDto>>

    fun getMovieDetail(movieId: Int): Flow<Result<DetailMovieResponse>>

}