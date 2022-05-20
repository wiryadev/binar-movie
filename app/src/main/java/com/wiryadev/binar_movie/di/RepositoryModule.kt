package com.wiryadev.binar_movie.di

import com.wiryadev.binar_movie.data.repositories.movie.MovieRepository
import com.wiryadev.binar_movie.data.repositories.movie.MovieRepositoryImpl
import com.wiryadev.binar_movie.data.repositories.tv.TvRepository
import com.wiryadev.binar_movie.data.repositories.tv.TvRepositoryImpl
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import com.wiryadev.binar_movie.data.repositories.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class,
        DataStoreModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindsMovieRepository(repository: MovieRepositoryImpl): MovieRepository

    @Binds
    abstract fun bindsTvRepository(repository: TvRepositoryImpl): TvRepository

}