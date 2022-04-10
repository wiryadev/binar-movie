package com.wiryadev.binar_movie.data.remote.tv

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto

interface TvRepository {

    fun discoverTvShows(): LiveData<PagingData<TvDto>>

}