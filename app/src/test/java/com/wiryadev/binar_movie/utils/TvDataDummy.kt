package com.wiryadev.binar_movie.utils

import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.remote.tv.dto.DetailTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.ListTvResponse
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto

object TvDataDummy {

    val listTvResponse = ListTvResponse(
        page = 1,
        tvShows = generateTvDtoList(),
        totalPages = 30,
        totalResults = 30
    )

    private fun generateTvDtoList() : List<TvDto> {
        val list = mutableListOf<TvDto>()
        for (i in 1..30) {
            list.add(
                TvDto(
                    backdropPath = "/backdropTv$i",
                    firstAirDate = "",
                    genreIds = listOf(),
                    id = i,
                    name = "Title $i",
                    originCountry = listOf(),
                    originalLanguage = "",
                    originalName = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "/posterTv$i",
                    voteAverage = 0.0,
                    voteCount = 0
                )
            )
        }
        return list
    }

    val detailTv = DetailTvResponse(
        adult = false,
        backdropPath = "/detailTv",
        episodeRunTime = listOf(),
        firstAirDate = "",
        genres = listOf(),
        homepage = "",
        id = 1,
        inProduction = false,
        lastAirDate = "",
        name = "Title 1",
        numberOfEpisodes = 8,
        numberOfSeasons = 4,
        originalLanguage = "",
        originalName = "",
        overview = "",
        popularity = 0.0,
        posterPath = "",
        status = "",
        tagline = "",
        type = "",
        voteAverage = 0.0,
        voteCount = 0
    )

    val favoriteTvs = listOf(
        TvEntity(
            tvId = 1,
            title = "Title 1",
            posterPath = "/tv1"
        )
    )

}