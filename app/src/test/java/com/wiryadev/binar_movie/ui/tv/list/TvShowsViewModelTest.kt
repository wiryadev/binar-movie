package com.wiryadev.binar_movie.ui.tv.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.TvDataDummy
import com.wiryadev.binar_movie.utils.getOrAwaitValue
import com.wiryadev.binar_movie.utils.noopListUpdateCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class TvShowsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    private val viewModel: TvShowsViewModel = mock()

    @Test
    fun `when Get Tv Shows, should return Success`() = runTest {
        val fakeData = TvDataDummy.listTvResponse.tvShows
        val pagingData = TvPagedTestDataSources.snapshot(fakeData)
        val expected = MutableLiveData<PagingData<TvDto>>()
        expected.value = pagingData

        whenever(viewModel.tvShows)
            .doReturn(expected)
        val actual = viewModel.tvShows.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = TvShowsAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(actual)

        verify(viewModel).tvShows
        differ.snapshot() shouldNotBe null
        differ.snapshot().size shouldBeEqualTo fakeData.size
        differ.snapshot()[0]?.id shouldBeEqualTo fakeData[0].id
    }
}

class TvPagedTestDataSources : PagingSource<Int, LiveData<List<TvDto>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<TvDto>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<TvDto>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<TvDto>): PagingData<TvDto> {
            return PagingData.from(items)
        }
    }
}