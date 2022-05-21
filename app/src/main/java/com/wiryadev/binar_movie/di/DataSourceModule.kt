package com.wiryadev.binar_movie.di

import com.wiryadev.binar_movie.data.local.FavoriteLocalDataSource
import com.wiryadev.binar_movie.data.local.FavoriteLocalDataSourceImpl
import com.wiryadev.binar_movie.data.local.UserLocalDataSource
import com.wiryadev.binar_movie.data.local.UserLocalDataSourceImpl
import com.wiryadev.binar_movie.data.preference.UserPreferenceDataStore
import com.wiryadev.binar_movie.data.preference.UserPreferenceDataStoreImpl
import com.wiryadev.binar_movie.data.remote.movie.MovieRemoteDataSource
import com.wiryadev.binar_movie.data.remote.movie.MovieRemoteDataSourceImpl
import com.wiryadev.binar_movie.data.remote.tv.TvRemoteDataSource
import com.wiryadev.binar_movie.data.remote.tv.TvRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsMovieRemoteDataSource(
        movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl
    ): MovieRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsTvRemoteDataSource(
        tvRemoteDataSourceImpl: TvRemoteDataSourceImpl
    ): TvRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsFavoriteLocalDataSource(
        favoriteLocalDataSourceImpl: FavoriteLocalDataSourceImpl
    ): FavoriteLocalDataSource

    @Binds
    @Singleton
    abstract fun bindsUserLocalDataSource(
        userLocalDataSourceImpl: UserLocalDataSourceImpl
    ): UserLocalDataSource

    @Binds
    @Singleton
    abstract fun bindsUserPreferenceDataStore(
        userPreferenceDataStoreImpl: UserPreferenceDataStoreImpl
    ): UserPreferenceDataStore

}