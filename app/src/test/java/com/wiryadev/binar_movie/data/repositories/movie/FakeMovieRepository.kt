package com.wiryadev.binar_movie.data.repositories.movie

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.movie.MovieRemoteDataSource
import com.wiryadev.binar_movie.data.remote.movie.MoviesPagingSource
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import com.wiryadev.binar_movie.utils.MovieDataDummy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock

class FakeMovieRepository : MovieRepository {

    private val favoriteMovieList = mutableListOf<MovieEntity>()

    private val remoteDataSource = mock<MovieRemoteDataSource>()

    override fun discoverMovies(): LiveData<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MoviesPagingSource(remoteDataSource = remoteDataSource)
            }
        ).liveData
    }

    override fun getMovieDetail(movieId: Int): Flow<Result<DetailMovieResponse>> {
        return flowOf(Result.Success(MovieDataDummy.detailMovie))
    }

    override fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return flowOf(MovieDataDummy.favoriteMovies)
    }

    override fun checkFavoriteMovie(id: Int): Flow<Int> {
        val result = favoriteMovieList.find {
            it.movieId == id
        }
        return flowOf(
            if (result != null) 1 else 0
        )
    }

    override suspend fun addFavoriteMovie(movie: MovieEntity) {
        favoriteMovieList.add(movie)
    }

    override suspend fun deleteFavoriteMovie(movie: MovieEntity) {
        favoriteMovieList.remove(movie)
    }
}