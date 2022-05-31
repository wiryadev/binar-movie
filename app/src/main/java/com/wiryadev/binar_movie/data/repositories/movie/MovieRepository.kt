package com.wiryadev.binar_movie.data.repositories.movie

import androidx.paging.PagingData
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun discoverMovies(): Flow<PagingData<MovieDto>>

    fun getMovieDetail(movieId: Int): Flow<Result<DetailMovieResponse>>

    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    fun checkFavoriteMovie(id: Int): Flow<Int>

    suspend fun addFavoriteMovie(movie: MovieEntity)

    suspend fun deleteFavoriteMovie(movie: MovieEntity)

}