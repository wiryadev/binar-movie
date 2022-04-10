package com.wiryadev.binar_movie.data.remote

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wiryadev.binar_movie.data.MovieRepository
import com.wiryadev.binar_movie.data.remote.dto.MovieDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : MovieRepository {

    override fun discoverMovies(): LiveData<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE
            ),
            pagingSourceFactory = {
                MoviesPagingSource(movieService = movieService)
            }
        ).liveData
    }
}

private const val PAGING_PAGE_SIZE = 20