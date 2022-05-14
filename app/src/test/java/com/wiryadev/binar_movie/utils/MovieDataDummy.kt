package com.wiryadev.binar_movie.utils

import com.wiryadev.binar_movie.data.local.entity.MovieEntity

object MovieDataDummy {

    val favoriteMovies = listOf(
        MovieEntity(
            movieId = 1,
            title = "Title 1",
            posterPath = "/movie1"
        )
    )

}