package com.wiryadev.binar_movie.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.UserDataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: UserRepository = mock()
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when uiState is initialized, then Loading is false`() = runTest {
        viewModel.uiState.test {
            awaitItem().isLoading shouldBeEqualTo false
        }
    }

    @Test
    fun `when uiState is initialized, then Success is false`() = runTest {
        viewModel.uiState.test {
            awaitItem().isSuccess shouldBeEqualTo false
        }
    }

    @Test
    fun `when uiState is initialized, then Error Message is null`() = runTest {
        viewModel.uiState.test {
            awaitItem().errorMessage shouldBeEqualTo null
        }
    }

    @Test
    fun `when Register success, should return True`() = runTest {
        viewModel.register(UserDataDummy.userEntity)
        verify(repository, times(1)).register(UserDataDummy.userEntity)

        val actual = viewModel.uiState.value
        actual shouldNotBe null
        actual.isSuccess shouldBeEqualTo true
        actual.errorMessage shouldBeEqualTo null
    }

    @Test
    fun `when Register failed, should return False and throw Exception`() = runTest {
        whenever(repository.register(UserDataDummy.userEntity))
            .thenThrow(RuntimeException("Registration Failed"))
        viewModel.register(UserDataDummy.userEntity)

        val actual = viewModel.uiState.value
        actual shouldNotBe null
        actual.isSuccess shouldBeEqualTo false
        actual.errorMessage shouldBeEqualTo "Registration Failed"
    }

}