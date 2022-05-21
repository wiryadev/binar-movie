package com.wiryadev.binar_movie.data.repositories.tv

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.tv.TvPagingSource
import com.wiryadev.binar_movie.data.remote.tv.TvRemoteDataSource
import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto
import com.wiryadev.binar_movie.utils.TvDataDummy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock

class FakeTvRepository : TvRepository {

    private val favoriteTvList = mutableListOf<TvEntity>()

    private val remoteDataSource = mock<TvRemoteDataSource>()

    override fun discoverTvShows(): LiveData<PagingData<TvDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                TvPagingSource(remoteDataSource = remoteDataSource)
            }
        ).liveData
    }

    override fun getTvShowDetail(tvId: Int): Flow<Result<DetailTvResponse>> {
        return flowOf(Result.Success(TvDataDummy.detailTv))
    }

    override fun getFavoriteTvs(): Flow<List<TvEntity>> {
        return flowOf(TvDataDummy.favoriteTvs)
    }

    override fun checkFavoriteTv(id: Int): Flow<Int> {
        val result = favoriteTvList.find {
            it.tvId == id
        }
        return flowOf(
            if (result != null) 1 else 0
        )
    }

    override suspend fun addFavoriteTv(tv: TvEntity) {
        favoriteTvList.add(tv)
    }

    override suspend fun deleteFavoriteTv(tv: TvEntity) {
        favoriteTvList.remove(tv)
    }
}