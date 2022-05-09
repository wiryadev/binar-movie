package com.wiryadev.binar_movie.data.local.db

import androidx.room.*
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    /**
     * Movie
     */
    @Query("SELECT * FROM tableMovie WHERE email=:email")
    fun getFavoriteMovies(email: String): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM tableMovie WHERE id=:id)")
    fun checkFavoriteMovie(id: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFavoriteMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteFavoriteMovie(movie: MovieEntity)

    /**
     * TV
     */
    @Query("SELECT * FROM tableTv WHERE email=:email")
    fun getFavoriteTvs(email: String): Flow<List<TvEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM tableTv WHERE id=:id)")
    fun checkFavoriteTv(id: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFavoriteTv(tv: TvEntity)

    @Delete
    suspend fun deleteFavoriteTv(tv: TvEntity)

}