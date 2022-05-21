package com.wiryadev.binar_movie.data.repositories.tv

import androidx.paging.PagingSource
import com.wiryadev.binar_movie.data.local.FakeFavoriteDao
import com.wiryadev.binar_movie.data.local.db.FavoriteDao
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.remote.FakeTvRemoteDataSource
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.remote.tv.TvPagingSource
import com.wiryadev.binar_movie.data.remote.tv.TvRemoteDataSource
import com.wiryadev.binar_movie.data.remote.tv.TvService
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.TvDataDummy
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
class TvRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val remoteDataSource: TvRemoteDataSource = FakeTvRemoteDataSource()
    private val favoriteDao: FavoriteDao = FakeFavoriteDao()

    private lateinit var repository: TvRepository

    @Before
    fun setUp() {
        repository = TvRepositoryImpl(
            remoteDataSource = remoteDataSource,
            favoriteDao = favoriteDao,
        )
    }

    @Test
    fun `when Load Paging Tvs, should return Page contains intended data`() = runTest {
        val pagingSource = TvPagingSource(remoteDataSource)
        val dummy = TvDataDummy.generateDynamicTvList(2).tvShows
        val expected = PagingSource.LoadResult.Page(
            data = dummy,
            prevKey = null,
            nextKey = dummy[1].id
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false,
            )
        )
        actual shouldBeEqualTo expected
    }

    @Test
    fun `when Get Tv Detail success, should return Tv`() = runTest {
        val expected = Result.Success(TvDataDummy.detailTv)
        val actual = repository.getTvShowDetail(TvDataDummy.detailTv.id).first()

        actual shouldBeEqualTo expected
    }

    @Test(expected = RuntimeException::class)
    fun `when Get Tv Detail failed, should throw Error`() = runTest {
        val expected = Result.Error(RuntimeException("Not Found"))
        val actual = repository.getTvShowDetail(-1).first()

        actual shouldBeEqualTo expected
    }

    @Test
    fun `when Get Favorite Tvs, should return Favorite Tv List`() = runTest {
        val expected = TvDataDummy.favoriteTvs
        val actual = repository.getFavoriteTvs().first()

        actual shouldBeEqualTo expected
        actual.size shouldBeEqualTo expected.size
    }

    @Test
    fun `when Add Favorite and then Check, should return 1`() = runTest {
        val fakeResponse = TvDataDummy.generateDetailTvResponse(99)
        val dataToBeAdded = TvEntity(
            tvId = fakeResponse.id,
            title = fakeResponse.name,
            posterPath = fakeResponse.posterPath,
        )
        launch { repository.addFavoriteTv(dataToBeAdded) }
        advanceUntilIdle()

        val actual = repository.checkFavoriteTv(dataToBeAdded.tvId).first()
        actual shouldBeEqualTo 1
    }

    @Test
    fun `when Delete newly added Favorite and then Check, should return 0`() = runTest {
        val fakeResponse = TvDataDummy.generateDetailTvResponse(616)
        val dataToBeAddedAndDeleted = TvEntity(
            tvId = fakeResponse.id,
            title = fakeResponse.name,
            posterPath = fakeResponse.posterPath,
        )

        launch { repository.addFavoriteTv(dataToBeAddedAndDeleted) }
        launch { repository.deleteFavoriteTv(dataToBeAddedAndDeleted) }
        advanceUntilIdle()

        val actual = repository.checkFavoriteTv(dataToBeAddedAndDeleted.tvId).first()
        actual shouldBeEqualTo 0
    }

    @Test
    fun `when Delete existing Favorite and then Check, should return 0`() = runTest {
        val existingDataToBeDeleted = TvDataDummy.favoriteTvs[0]

        launch { repository.getFavoriteTvs() }
        launch { repository.deleteFavoriteTv(existingDataToBeDeleted) }
        advanceUntilIdle()

        val actual = repository.checkFavoriteTv(existingDataToBeDeleted.tvId).first()
        actual shouldBeEqualTo 0
    }
}