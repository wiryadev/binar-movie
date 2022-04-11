package com.wiryadev.binar_movie.ui.tv.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wiryadev.binar_movie.data.repositories.tv.TvRepository
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    private val tvRepository: TvRepository
) : ViewModel() {

    val movies: LiveData<PagingData<TvDto>> =
        tvRepository.discoverTvShows().cachedIn(viewModelScope)

}