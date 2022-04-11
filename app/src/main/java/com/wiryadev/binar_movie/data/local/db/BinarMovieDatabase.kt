package com.wiryadev.binar_movie.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wiryadev.binar_movie.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
)
abstract class BinarMovieDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}