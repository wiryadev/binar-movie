package com.wiryadev.binar_movie.ui.entry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wiryadev.binar_movie.data.preference.AuthModel
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EntryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: UserRepository = mock()
    private lateinit var viewModel: EntryViewModel

    private val loggedInAuthModel = AuthModel(
        username = "username",
        email = "email"
    )
    private val emptyAuthModel = AuthModel(
        username = "",
        email = ""
    )

    @Before
    fun setUp() {
        viewModel = EntryViewModel(repository)
    }

    @Test
    fun `when Get User is logged in, should return True`() = runTest {
        whenever(repository.getUserSession())
            .thenReturn(flowOf(loggedInAuthModel))
        viewModel.getUser()

        val actual = viewModel.isLoggedIn.getOrAwaitValue()
        verify(repository).getUserSession()

        actual shouldBeEqualTo true
    }

    @Test
    fun `when Get User is empty, should return False`() = runTest {
        whenever(repository.getUserSession())
            .thenReturn(flowOf(emptyAuthModel))
        viewModel.getUser()

        val actual = viewModel.isLoggedIn.getOrAwaitValue()
        verify(repository).getUserSession()

        actual shouldBeEqualTo false
    }

}