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

    fun generateDynamicMovieList(n: Int) = ListMovieResponse(
        page = 1,
        movies = generateMovieDtoList(n),
        totalPages = n,
        totalResults = n
    )

    private fun generateMovieDtoList(n: Int = 30): List<MovieDto> {
        val list = mutableListOf<MovieDto>()
        for (i in 1..n) {
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

    val detailMovie = generateDetailMovieResponse()

    fun generateDetailMovieResponse(i: Int = 1) = DetailMovieResponse(
        adult = false,
        backdropPath = "/detailMovie$i",
        budget = i * 100000,
        genres = listOf(),
        homepage = "",
        id = i,
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
        title = "Title $i",
        video = false,
        voteAverage = 0.0,
        voteCount = 0
    )

    val favoriteMovies = generateFavoriteMovies()

    fun generateFavoriteMovies(n: Int = 10): List<MovieEntity> {
        val list = mutableListOf<MovieEntity>()
        for (i in 1..n) {
            list.add(
                MovieEntity(
                    movieId = i,
                    title = "Title $i",
                    posterPath = "/movie$i"
                )
            )
        }
        return list
    }

}