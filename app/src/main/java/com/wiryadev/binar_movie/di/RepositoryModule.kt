package com.wiryadev.binar_movie.di

import com.wiryadev.binar_movie.data.repositories.movie.MovieRepository
import com.wiryadev.binar_movie.data.repositories.movie.MovieRepositoryImpl
import com.wiryadev.binar_movie.data.repositories.tv.TvRepository
import com.wiryadev.binar_movie.data.repositories.tv.TvRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        NetworkModule::class,
    ]
)
abstract class RepositoryModule {

    @Binds
    abstract fun provideMovieRepository(repository: MovieRepositoryImpl): MovieRepository

    @Binds
    abstract fun provideTvRepository(repository: TvRepositoryImpl): TvRepository

}