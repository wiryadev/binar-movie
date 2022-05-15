package com.wiryadev.binar_movie.utils

import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.ListMovieResponse
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto

object MovieDataDummy {

    val listMovieResponse = ListMovieResponse(
        page = 1,
        movies = generateMovieDtoList(),
        totalPages = 30,
        totalResults = 30
    )

    private fun generateMovieDtoList() : List<MovieDto> {
        val list = mutableListOf<MovieDto>()
        for (i in 1..30) {
            list.add(
                MovieDto(
                    adult = false,
                    backdropPath = "/backdropMovie$i",
                    genreIds = listOf(),
                    id = i,
                    originalLanguage = "",
                    originalTitle = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "/posterMovie$i",
                    releaseDate = "",
                    title = "Title $i",
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            )
        }
        return list
    }

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