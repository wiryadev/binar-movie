package com.wiryadev.binar_movie.data.repositories.movie

//import androidx.paging.PagingSource

import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import com.wiryadev.binar_movie.data.remote.FakeMovieService
import com.wiryadev.binar_movie.data.remote.movie.MovieService
import com.wiryadev.binar_movie.data.remote.movie.MoviesPagingSource
import com.wiryadev.binar_movie.utils.MovieDataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieRepositoryTest {

    private val movieService: MovieService = FakeMovieService()

    @Test
    fun `when Load Paging Movies, should return Page contains intended data`() = runTest {
        val pagingSource = MoviesPagingSource(movieService)
        val dummy = MovieDataDummy.generateDynamicMovieList(2).movies
        val expected = Page(
            data = dummy,
            prevKey = null,
            nextKey = dummy[1].id
        )
        val actual = pagingSource.load(
            Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false,
            )
        )
        actual shouldBeEqualTo expected
    }

    @Test
    fun getMovieDetail() {
    }

    @Test
    fun getFavoriteMovies() {
    }

    @Test
    fun checkFavoriteMovie() {
    }

    @Test
    fun addFavoriteMovie() {
    }

    @Test
    fun deleteFavoriteMovie() {
    }
}