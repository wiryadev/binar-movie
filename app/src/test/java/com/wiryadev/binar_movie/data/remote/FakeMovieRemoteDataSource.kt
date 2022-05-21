package com.wiryadev.binar_movie.data.remote

import com.wiryadev.binar_movie.data.remote.movie.MovieRemoteDataSource
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.ListMovieResponse
import com.wiryadev.binar_movie.utils.MovieDataDummy

class FakeMovieRemoteDataSource : MovieRemoteDataSource {
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