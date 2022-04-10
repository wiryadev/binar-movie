package com.wiryadev.binar_movie.data.remote.tv

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto
import kotlinx.coroutines.flow.Flow

interface TvRepository {

    fun discoverTvShows(): LiveData<PagingData<TvDto>>

    fun getTvShowDetail(tvId: Int): Flow<Result<DetailTvResponse>>

}