package com.wiryadev.binar_movie.di

import com.wiryadev.binar_movie.data.MovieRepository
import com.wiryadev.binar_movie.data.remote.MovieRepositoryImpl
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
    abstract fun provideNoteRepository(repository: MovieRepositoryImpl): MovieRepository

}