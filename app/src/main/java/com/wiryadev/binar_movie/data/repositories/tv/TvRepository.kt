package com.wiryadev.binar_movie.data.repositories.tv

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto
import kotlinx.coroutines.flow.Flow

interface TvRepository {

    fun discoverTvShows(): LiveData<PagingData<TvDto>>

    fun getTvShowDetail(tvId: Int): Flow<Result<DetailTvResponse>>

    fun getFavoriteTvs(): Flow<List<TvEntity>>

    fun checkFavoriteTv(id: Int): Flow<Int>

    suspend fun addFavoriteTv(tv: TvEntity)

    suspend fun deleteFavoriteTv(tv: TvEntity)

}