package com.wiryadev.binar_movie.data.repositories.movie

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.movie.MovieService
import com.wiryadev.binar_movie.data.remote.movie.MoviesPagingSource
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    override fun getMovieDetail(movieId: Int): Flow<Result<DetailMovieResponse>> {
        return flow {
            try {
                val response = movieService.getMovieDetail(movieId = movieId)
                emit(Result.Success(data = response))
            } catch (e: Exception) {
                emit(Result.Error(exception = e))
            }
        }.flowOn(Dispatchers.IO)
    }

}

private const val PAGING_PAGE_SIZE = 20