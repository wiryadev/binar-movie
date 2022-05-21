package com.wiryadev.binar_movie.data.local

import com.wiryadev.binar_movie.data.local.db.FavoriteDao
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteLocalDataSourceImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
) : FavoriteLocalDataSource {

    override fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return favoriteDao.getFavoriteMovies()
    }

    override fun checkFavoriteMovie(id: Int): Flow<Int> {
        return favoriteDao.checkFavoriteMovie(id = id)
    }

    override suspend fun addFavoriteMovie(movie: MovieEntity) {
        return favoriteDao.addFavoriteMovie(movie = movie)
    }

    override suspend fun deleteFavoriteMovie(movie: MovieEntity) {
        return favoriteDao.deleteFavoriteMovie(movie = movie)
    }

    override fun getFavoriteTvs(): Flow<List<TvEntity>> {
        return favoriteDao.getFavoriteTvs()
    }

    override fun checkFavoriteTv(id: Int): Flow<Int> {
        return favoriteDao.checkFavoriteTv(id = id)
    }

    override suspend fun addFavoriteTv(tv: TvEntity) {
        return favoriteDao.addFavoriteTv(tv = tv)
    }

    override suspend fun deleteFavoriteTv(tv: TvEntity) {
        return favoriteDao.deleteFavoriteTv(tv = tv)
    }

}