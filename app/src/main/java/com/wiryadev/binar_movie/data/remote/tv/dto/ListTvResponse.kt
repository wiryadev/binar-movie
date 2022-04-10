package com.wiryadev.binar_movie.data.remote.tv.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListTvResponse(
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val tvShows: List<TvDto>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)