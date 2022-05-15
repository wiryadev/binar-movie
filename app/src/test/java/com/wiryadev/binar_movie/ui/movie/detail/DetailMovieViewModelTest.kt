package com.wiryadev.binar_movie.ui.movie.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.binar_movie.data.repositories.movie.FakeMovieRepository
import com.wiryadev.binar_movie.data.repositories.movie.MovieRepository
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.MovieDataDummy
import com.wiryadev.binar_movie.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailMovieViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: MovieRepository
    private lateinit var viewModel: DetailMovieViewModel

    private val successUiState = DetailMovieUiState(
        movie = MovieDataDummy.detailMovie
    )

    @Before
    fun setUp() {
        repository = FakeMovieRepository()
        viewModel = DetailMovieViewModel(repository)
    }

    @Test
    fun `when Get Detail Tv By Id, should return Success`() = runTest {
        val expected = MutableLiveData<DetailMovieUiState>()
        expected.value = successUiState

        viewModel.getDetail(1)
        val actual = viewModel.uiState.getOrAwaitValue()

        actual shouldNotBe null
        actual shouldBeEqualTo expected.value
        actual.errorMessage shouldBe null
        actual.movie shouldBeEqualTo expected.value!!.movie
    }

    @Test
    fun `when Add Favorite and then Check, should return True`() {
        viewModel.addFavoriteMovie(MovieDataDummy.detailMovie)
        viewModel.checkIsFavorite(id = MovieDataDummy.detailMovie.id)

        val actual = viewModel.uiState.getOrAwaitValue()
        actual.isFavorite shouldBe true
    }

    @Test
    fun `when Delete Favorite and then Check, should return False`() {
        viewModel.deleteFavoriteMovie(MovieDataDummy.detailMovie)
        viewModel.checkIsFavorite(id = MovieDataDummy.detailMovie.id)

        val actual = viewModel.uiState.getOrAwaitValue()
        actual.isFavorite shouldBe false
    }

}