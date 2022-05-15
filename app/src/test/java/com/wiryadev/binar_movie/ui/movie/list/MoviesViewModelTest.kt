package com.wiryadev.binar_movie.ui.movie.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.MovieDataDummy
import com.wiryadev.binar_movie.utils.getOrAwaitValue
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
class MoviesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    private val viewModel: MoviesViewModel = mock()

    @Test
    fun `when Get Movies, should return Success`() = runTest {
        val fakeData = MovieDataDummy.listMovieResponse.movies
        val pagingData = PagedTestDataSources.snapshot(fakeData)
        val expected = MutableLiveData<PagingData<MovieDto>>()
        expected.value = pagingData

        whenever(viewModel.movies)
            .doReturn(expected)
        val actual = viewModel.movies.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MoviesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(actual)

        verify(viewModel).movies
        differ.snapshot() shouldNotBe null
        differ.snapshot().size shouldBeEqualTo fakeData.size
        differ.snapshot()[0]?.id shouldBeEqualTo fakeData[0].id
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class PagedTestDataSources : PagingSource<Int, LiveData<List<MovieDto>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<MovieDto>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<MovieDto>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<MovieDto>): PagingData<MovieDto> {
            return PagingData.from(items)
        }
    }
}