package com.wiryadev.binar_movie.di

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

}