package com.wiryadev.binar_movie.data.remote.movie

import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.ListMovieResponse
import javax.inject.Inject
import javax.inject.Singleton

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieService: MovieService,
) : MovieRemoteDataSource {

    override suspend fun discoverMovies(page: Int, size: Int): ListMovieResponse {
        return movieService.discoverMovies(
            page = page,
            size = size,
        )
    }

    override suspend fun getMovieDetail(movieId: Int): DetailMovieResponse {
        return movieService.getMovieDetail(movieId = movieId)
    }

}