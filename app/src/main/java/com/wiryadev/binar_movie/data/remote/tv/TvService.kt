package com.wiryadev.binar_movie.data.remote.tv

import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.ListTvResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvService {

    @GET("discover/tv")
    suspend fun discoverTv(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ListTvResponse

    @GET("tv/{tv_id}")
    suspend fun getTvDetail(
        @Path("tv_id") tvId: Int,
    ): DetailTvResponse

}