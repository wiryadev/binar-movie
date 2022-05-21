package com.wiryadev.binar_movie.data.remote.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto

class MoviesPagingSource(
    private val remoteDataSource: MovieRemoteDataSource
) : PagingSource<Int, MovieDto>() {
    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = remoteDataSource.discoverMovies(position, params.loadSize).movies
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