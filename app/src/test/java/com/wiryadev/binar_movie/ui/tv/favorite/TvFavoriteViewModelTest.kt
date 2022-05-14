package com.wiryadev.binar_movie.ui.tv.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.binar_movie.data.repositories.tv.TvRepository
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.TvDataDummy
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
@RunWith(MockitoJUnitRunner::class)
class TvFavoriteViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: TvRepository = mock()
    private lateinit var viewModel: TvFavoriteViewModel

    private val successUiState = FavoriteTvUiState(
        tvShows = TvDataDummy.favoriteTvs
    )

    @Before
    fun setUp() {
        whenever(repository.getFavoriteTvs())
            .doReturn(flowOf(TvDataDummy.favoriteTvs))
        viewModel = TvFavoriteViewModel(repository)
    }

    @Test
    fun `when Get Favorite Tv Shows, should return Success`() = runTest {
        val expected = MutableLiveData<FavoriteTvUiState>()
        expected.value = successUiState

        val actual = viewModel.uiState.getOrAwaitValue()
        verify(repository).getFavoriteTvs()

        actual shouldNotBe null
        actual shouldBeEqualTo expected.value
        actual.errorMessage shouldBe null
        actual.tvShows shouldBeEqualTo expected.value!!.tvShows
    }

}