package com.wiryadev.binar_movie.data.local

import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteLocalDataSource {

    /**
     * Movie
     */
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    fun checkFavoriteMovie(id: Int): Flow<Int>

    suspend fun addFavoriteMovie(movie: MovieEntity)

    suspend fun deleteFavoriteMovie(movie: MovieEntity)

    /**
     * TV
     */
    fun getFavoriteTvs(): Flow<List<TvEntity>>

    fun checkFavoriteTv(id: Int): Flow<Int>

    suspend fun addFavoriteTv(tv: TvEntity)

    suspend fun deleteFavoriteTv(tv: TvEntity)
}