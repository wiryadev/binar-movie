package com.wiryadev.binar_movie.ui.tv.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.binar_movie.data.repositories.tv.FakeTvRepository
import com.wiryadev.binar_movie.data.repositories.tv.TvRepository
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.TvDataDummy
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
class DetailTvViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: TvRepository
    private lateinit var viewModel: DetailTvViewModel

    private val successUiState = DetailTvUiState(
        tv = TvDataDummy.detailTv
    )

    @Before
    fun setUp() {
        repository = FakeTvRepository()
        viewModel = DetailTvViewModel(repository)
    }

    @Test
    fun `when Get Detail Tv By Id, should return Success`() = runTest {
        val expected = MutableLiveData<DetailTvUiState>()
        expected.value = successUiState

        viewModel.getDetail(1)
        val actual = viewModel.uiState.getOrAwaitValue()

        actual shouldNotBe null
        actual shouldBeEqualTo expected.value
        actual.errorMessage shouldBe null
        actual.tv shouldBeEqualTo expected.value!!.tv
    }

    @Test
    fun `when Add Favorite and then Check, should return True`() {
        viewModel.addFavoriteTv(TvDataDummy.detailTv)
        viewModel.checkIsFavorite(id = TvDataDummy.detailTv.id)

        val actual = viewModel.uiState.getOrAwaitValue()
        actual.isFavorite shouldBe true
    }

    @Test
    fun `when Delete Favorite and then Check, should return False`() {
        viewModel.addFavoriteTv(TvDataDummy.detailTv)
        viewModel.deleteFavoriteTv(TvDataDummy.detailTv)
        viewModel.checkIsFavorite(id = TvDataDummy.detailTv.id)

        val actual = viewModel.uiState.getOrAwaitValue()
        actual.isFavorite shouldBe false
    }

}