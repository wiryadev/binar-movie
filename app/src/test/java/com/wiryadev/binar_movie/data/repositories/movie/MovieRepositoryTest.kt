package com.wiryadev.binar_movie.data.repositories.movie

import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import com.wiryadev.binar_movie.data.local.FakeFavoriteDao
import com.wiryadev.binar_movie.data.local.db.FavoriteDao
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.remote.FakeMovieService
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.movie.MovieService
import com.wiryadev.binar_movie.data.remote.movie.MoviesPagingSource
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.MovieDataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val movieService: MovieService = FakeMovieService()
    private val favoriteDao: FavoriteDao = FakeFavoriteDao()

    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        repository = MovieRepositoryImpl(
            movieService = movieService,
            favoriteDao = favoriteDao,
        )
    }

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
    fun `when Get Movie Detail success, should return Movie`() = runTest {
        val expected = Result.Success(MovieDataDummy.detailMovie)
        val actual = repository.getMovieDetail(MovieDataDummy.detailMovie.id).first()

        actual shouldBeEqualTo expected
    }

    @Test(expected = RuntimeException::class)
    fun `when Get Movie Detail failed, should throw Error`() = runTest {
        val expected = Result.Error(RuntimeException("Not Found"))
        val actual = repository.getMovieDetail(-1).first()

        actual shouldBeEqualTo expected
    }

    @Test
    fun `when Get Favorite Movies, should return Favorite Movie List`() = runTest {
        val expected = MovieDataDummy.favoriteMovies
        val actual = repository.getFavoriteMovies().first()

        actual shouldBeEqualTo expected
        actual.size shouldBeEqualTo expected.size
    }

    @Test
    fun `when Add Favorite and then Check, should return 1`() = runTest {
        val fakeResponse = MovieDataDummy.generateDetailMovieResponse(99)
        val dataToBeAdded = MovieEntity(
            movieId = fakeResponse.id,
            title = fakeResponse.title,
            posterPath = fakeResponse.posterPath,
        )
        launch { repository.addFavoriteMovie(dataToBeAdded) }
        advanceUntilIdle()

        val actual = repository.checkFavoriteMovie(dataToBeAdded.movieId).first()
        actual shouldBeEqualTo 1
    }

    @Test
    fun `when Delete newly added Favorite and then Check, should return 0`() = runTest {
        val fakeResponse = MovieDataDummy.generateDetailMovieResponse(616)
        val dataToBeAddedAndDeleted = MovieEntity(
            movieId = fakeResponse.id,
            title = fakeResponse.title,
            posterPath = fakeResponse.posterPath,
        )

        launch { repository.addFavoriteMovie(dataToBeAddedAndDeleted) }
        launch { repository.deleteFavoriteMovie(dataToBeAddedAndDeleted) }
        advanceUntilIdle()

        val actual = repository.checkFavoriteMovie(dataToBeAddedAndDeleted.movieId).first()
        actual shouldBeEqualTo 0
    }

    @Test
    fun `when Delete existing Favorite and then Check, should return 0`() = runTest {
        val existingDataToBeDeleted = MovieDataDummy.favoriteMovies[0]

        launch { repository.getFavoriteMovies() }
        launch { repository.deleteFavoriteMovie(existingDataToBeDeleted) }
        advanceUntilIdle()

        val actual = repository.checkFavoriteMovie(existingDataToBeDeleted.movieId).first()
        actual shouldBeEqualTo 0
    }

}