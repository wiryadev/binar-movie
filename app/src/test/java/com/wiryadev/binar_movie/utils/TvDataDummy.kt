package com.wiryadev.binar_movie.utils

import com.wiryadev.binar_movie.data.local.entity.TvEntity

object TvDataDummy {

    val favoriteTvs = listOf(
        TvEntity(
            tvId = 1,
            title = "Title 1",
            posterPath = "/tv1"
        )
    )

}