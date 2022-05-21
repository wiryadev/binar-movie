package com.wiryadev.binar_movie.data.local

import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.utils.MovieDataDummy
import com.wiryadev.binar_movie.utils.TvDataDummy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeFavoriteLocalDataSource : FavoriteLocalDataSource {

    private val favoriteMovieList = mutableListOf<MovieEntity>()
    private val favoriteTvList = mutableListOf<TvEntity>()

    override fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        favoriteMovieList.addAll(
            MovieDataDummy.favoriteMovies
        )
        return flowOf(favoriteMovieList)
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