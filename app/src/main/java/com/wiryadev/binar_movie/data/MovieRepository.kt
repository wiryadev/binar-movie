package com.wiryadev.binar_movie.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wiryadev.binar_movie.data.remote.dto.MovieDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun discoverMovies(): LiveData<PagingData<MovieDto>>
}