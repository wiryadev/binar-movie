package com.wiryadev.binar_movie.data.remote.movie

import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.ListMovieResponse
import com.wiryadev.binar_movie.utils.wrapEspressoIdlingResource
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieService: MovieService,
) : MovieRemoteDataSource {

    override suspend fun discoverMovies(page: Int, size: Int): ListMovieResponse {
        wrapEspressoIdlingResource {
            return movieService.discoverMovies(
                page = page,
                size = size,
            )
        }
    }

    override suspend fun getMovieDetail(movieId: Int): DetailMovieResponse {
        wrapEspressoIdlingResource {
            return movieService.getMovieDetail(movieId = movieId)
        }
    }

}