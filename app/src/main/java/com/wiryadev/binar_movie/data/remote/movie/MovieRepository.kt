package com.wiryadev.binar_movie.data.remote.movie

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto

interface MovieRepository {

    fun discoverMovies(): LiveData<PagingData<MovieDto>>
}