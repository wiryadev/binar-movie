package com.wiryadev.binar_movie.data.repositories.tv

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wiryadev.binar_movie.data.local.db.FavoriteDao
import com.wiryadev.binar_movie.data.local.entity.TvEntity
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
    private val tvService: TvService,
    private val favoriteDao: FavoriteDao,
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

    override fun getFavoriteTvs(): Flow<List<TvEntity>> {
        return favoriteDao.getFavoriteTvs()
    }

    override fun checkFavoriteTv(id: Int): Flow<Int> {
        return favoriteDao.checkFavoriteTv(id = id)
    }

    override suspend fun addFavoriteTv(tv: TvEntity) {
        favoriteDao.addFavoriteTv(tv = tv)
    }

    override suspend fun deleteFavoriteTv(tv: TvEntity) {
        favoriteDao.deleteFavoriteTv(tv = tv)
    }

}

private const val PAGING_PAGE_SIZE = 20