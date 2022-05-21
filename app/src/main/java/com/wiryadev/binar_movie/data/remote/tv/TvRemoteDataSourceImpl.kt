package com.wiryadev.binar_movie.data.remote.tv

import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.ListTvResponse
import javax.inject.Inject
import javax.inject.Singleton

class TvRemoteDataSourceImpl @Inject constructor(
    private val tvService: TvService,
) : TvRemoteDataSource {

    override suspend fun discoverTv(page: Int, size: Int): ListTvResponse {
        return tvService.discoverTv(
            page = page,
            size = size,
        )
    }

    override suspend fun getTvDetail(tvId: Int): DetailTvResponse {
        return tvService.getTvDetail(tvId = tvId)
    }

}