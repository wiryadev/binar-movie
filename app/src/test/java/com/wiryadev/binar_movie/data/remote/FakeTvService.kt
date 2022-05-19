package com.wiryadev.binar_movie.data.remote

import com.wiryadev.binar_movie.data.remote.tv.TvService
import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.ListTvResponse
import com.wiryadev.binar_movie.utils.TvDataDummy

class FakeTvService : TvService {
    override suspend fun discoverTv(page: Int, size: Int): ListTvResponse {
        return TvDataDummy.generateDynamicTvList(2)
    }

    override suspend fun getTvDetail(tvId: Int): DetailTvResponse {
        return if (tvId == TvDataDummy.detailTv.id) {
            TvDataDummy.detailTv
        } else {
            throw RuntimeException("Not Found")
        }
    }
}