package com.wiryadev.binar_movie.utils

import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse

object MovieDataDummy {

    val detailMovie = DetailMovieResponse(
        adult = false,
        backdropPath = "/detailMovie",
        budget = 0,
        genres = listOf(),
        homepage = "",
        id = 1,
        imdbId = "",
        originalLanguage = "",
        originalTitle = "",
        overview = "",
        popularity = 0.0,
        posterPath = "",
        releaseDate = "",
        revenue = 0,
        runtime = 0,
        status = "",
        tagline = "",
        title = "Title 1",
        video = false,
        voteAverage = 0.0,
        voteCount = 0
    )

    val favoriteMovies = listOf(
        MovieEntity(
            movieId = 1,
            title = "Title 1",
            posterPath = "/movie1"
        )
    )

}