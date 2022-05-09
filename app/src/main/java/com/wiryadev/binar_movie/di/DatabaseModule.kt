package com.wiryadev.binar_movie.di

import android.content.Context
import androidx.room.Room
import com.wiryadev.binar_movie.data.local.db.BinarMovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): BinarMovieDatabase = Room
        .databaseBuilder(
            appContext,
            BinarMovieDatabase::class.java,
            "binar_movie_db"
        )
        .build()

    @Singleton
    @Provides
    fun provideUserDao(database: BinarMovieDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideFavoriteDao(database: BinarMovieDatabase) = database.favoriteDao()

}