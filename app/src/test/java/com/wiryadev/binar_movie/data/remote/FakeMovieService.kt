package com.wiryadev.binar_movie.data.remote

import com.wiryadev.binar_movie.data.remote.movie.MovieService
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.ListMovieResponse
import com.wiryadev.binar_movie.utils.MovieDataDummy

class FakeMovieService : MovieService {
    override suspend fun discoverMovies(page: Int, size: Int): ListMovieResponse {
        return MovieDataDummy.generateDynamicMovieList(2)
    }

    override suspend fun getMovieDetail(movieId: Int): DetailMovieResponse {
        return if (movieId == MovieDataDummy.detailMovie.id) {
            MovieDataDummy.detailMovie
        } else {
            throw RuntimeException("Not Found")
        }
    }
}