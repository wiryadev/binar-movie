package com.wiryadev.binar_movie.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.UserDataDummy
import com.wiryadev.binar_movie.utils.UserDataDummy.authModel
import com.wiryadev.binar_movie.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: UserRepository = mock()
    private lateinit var viewModel: LoginViewModel

    private val loggedInUiState = LoginUiState(
        isLoggedIn = true,
        isLoading = false,
        errorMessage = null,
    )

    @Before
    fun setUp() {
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `when Login success, should return True and Save User Session`() = runTest {
        val expected = MutableLiveData<LoginUiState>()
        expected.value = loggedInUiState

        whenever(repository.login(authModel.email, authModel.username))
            .thenReturn(flowOf(UserDataDummy.userEntity))
        viewModel.login(authModel.email, authModel.username)
        val actual = viewModel.uiState.getOrAwaitValue()

        verify(repository).saveUserSession(authModel)
        actual shouldNotBe null
        actual shouldBeEqualTo expected.value
        actual.isLoggedIn shouldBeEqualTo true
    }

    @Test
    fun `when Login failed, should return False and throw Exception`() = runTest {
        whenever(repository.login("", ""))
            .thenThrow(RuntimeException())
        viewModel.login("", "")
        val actual = viewModel.uiState.getOrAwaitValue()

        verify(repository, never()).saveUserSession(authModel)
        actual shouldNotBe null
        actual.isLoggedIn shouldBeEqualTo false
        actual.errorMessage shouldBeEqualTo "User Not Found"
    }

}