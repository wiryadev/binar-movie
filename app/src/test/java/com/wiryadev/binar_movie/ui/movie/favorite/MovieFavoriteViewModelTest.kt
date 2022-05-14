package com.wiryadev.binar_movie.ui.movie.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.binar_movie.data.repositories.movie.MovieRepositoryImpl
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.MovieDataDummy
import com.wiryadev.binar_movie.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class MovieFavoriteViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: MovieRepositoryImpl = mock()
    private lateinit var viewModel: MovieFavoriteViewModel

    private val successUiState = FavoriteMovieUiState(
        movies = MovieDataDummy.favoriteMovies
    )

    @Before
    fun setUp() {
        whenever(repository.getFavoriteMovies())
            .doReturn(flowOf(MovieDataDummy.favoriteMovies))
        viewModel = MovieFavoriteViewModel(repository)
    }

    @Test
    fun `when Get Favorite Movies, should return Success`() = runTest {
        val expected = MutableLiveData<FavoriteMovieUiState>()
        expected.value = successUiState

        val actual = viewModel.uiState.getOrAwaitValue()
        verify(repository).getFavoriteMovies()

        actual shouldNotBe null
        actual shouldBeEqualTo expected.value
        actual.errorMessage shouldBe null
        actual.movies shouldBeEqualTo expected.value!!.movies
    }

}