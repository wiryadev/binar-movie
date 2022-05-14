package com.wiryadev.binar_movie.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        MovieEntity::class,
        TvEntity::class,
    ],
    version = 2,
)
abstract class BinarMovieDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao

}