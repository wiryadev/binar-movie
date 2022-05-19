package com.wiryadev.binar_movie.utils

import com.wiryadev.binar_movie.data.local.entity.MovieEntity
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

    fun generateDynamicTvList(n: Int) = ListTvResponse(
        page = 1,
        tvShows = generateTvDtoList(n),
        totalPages = n,
        totalResults = n
    )

    private fun generateTvDtoList(n: Int = 30) : List<TvDto> {
        val list = mutableListOf<TvDto>()
        for (i in 1..n) {
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

    val detailTv = generateDetailTvResponse()

    fun generateDetailTvResponse(i: Int = 1) = DetailTvResponse(
        adult = false,
        backdropPath = "/detailTv$i",
        episodeRunTime = listOf(),
        firstAirDate = "",
        genres = listOf(),
        homepage = "",
        id = i,
        inProduction = false,
        lastAirDate = "",
        name = "Title $i",
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

    val favoriteTvs = generateFavoriteTvs()

    fun generateFavoriteTvs(n: Int = 10): List<TvEntity> {
        val list = mutableListOf<TvEntity>()
        for (i in 1..n) {
            list.add(
                TvEntity(
                    tvId = i,
                    title = "Title $i",
                    posterPath = "/movie$i"
                )
            )
        }
        return list
    }

}