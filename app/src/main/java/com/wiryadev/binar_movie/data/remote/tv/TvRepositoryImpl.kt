package com.wiryadev.binar_movie.data.remote.tv

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto
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
}

private const val PAGING_PAGE_SIZE = 20