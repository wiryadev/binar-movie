package com.wiryadev.binar_movie.data.remote.tv

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto

class TvPagingSource(
    private val tvService: TvService
) : PagingSource<Int, TvDto>() {
    override fun getRefreshKey(state: PagingState<Int, TvDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvDto> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = tvService.discoverTv(position, params.loadSize).tvShows
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}

private const val INITIAL_PAGE_INDEX = 1