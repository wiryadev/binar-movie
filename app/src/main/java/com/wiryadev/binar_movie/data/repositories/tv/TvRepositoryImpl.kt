package com.wiryadev.binar_movie.data.repositories.tv

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.tv.TvPagingSource
import com.wiryadev.binar_movie.data.remote.tv.TvService
import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TvRepositoryImpl @Inject constructor(
    private val tvService: TvService
) : TvRepository {

    override fun discoverTvShows(): LiveData<PagingData<TvDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE
            ),
            pagingSourceFactory = {
                TvPagingSource(tvService = tvService)
            }
        ).liveData
    }

    override fun getTvShowDetail(tvId: Int): Flow<Result<DetailTvResponse>> {
        return flow {
            try {
                val response = tvService.getTvDetail(tvId = tvId)
                emit(Result.Success(data = response))
            } catch (e: Exception) {
                emit(Result.Error(exception = e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

private const val PAGING_PAGE_SIZE = 20