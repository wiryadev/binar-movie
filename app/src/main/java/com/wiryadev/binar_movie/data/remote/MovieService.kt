package com.wiryadev.binar_movie.data.remote

import com.wiryadev.binar_movie.data.remote.dto.ListMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ListMovieResponse

}