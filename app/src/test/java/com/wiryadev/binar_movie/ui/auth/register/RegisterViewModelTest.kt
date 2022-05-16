package com.wiryadev.binar_movie.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.UserDataDummy
import com.wiryadev.binar_movie.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: UserRepository = mock()
    private lateinit var viewModel: RegisterViewModel

    private val registerUiState = RegisterUiState(
        isLoading = false,
        isSuccess = true,
        errorMessage = null,
    )

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when Register, should Success return True`() = runTest {
        val expected = MutableLiveData<RegisterUiState>()
        expected.value = registerUiState

        viewModel.register(UserDataDummy.userEntity)
        val actual = viewModel.uiState.getOrAwaitValue()

        verify(repository, times(1)).register(UserDataDummy.userEntity)
        actual shouldNotBe null
        actual shouldBeEqualTo expected.value
        actual.isSuccess shouldBeEqualTo true
    }

    @Test
    fun `when Register, should Failed and return False`() = runTest {
        whenever(repository.register(UserDataDummy.userEntity))
            .thenThrow(RuntimeException("Registration Failed"))
        viewModel.register(UserDataDummy.userEntity)
        val actual = viewModel.uiState.getOrAwaitValue()

        actual shouldNotBe null
        actual.isSuccess shouldBeEqualTo false
        actual.errorMessage shouldBeEqualTo "Registration Failed"
    }

}