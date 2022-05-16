package com.wiryadev.binar_movie.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.data.repositories.user.UserRepository
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.UserDataDummy
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: UserRepository = mock()
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        viewModel = ProfileViewModel(repository)
    }

    @Test
    fun `when Get User Session, should return Authenticated User`() = runTest {
        whenever(repository.getUserSession())
            .thenReturn(flowOf(UserDataDummy.authModel))
        viewModel.checkUserSession()
        val actual = viewModel.userSession.getOrAwaitValue()

        actual shouldNotBe null
        actual.email shouldBeEqualTo UserDataDummy.authModel.email
        actual.username shouldBeEqualTo UserDataDummy.authModel.username
    }

    @Test
    fun `when Logout, should invoke Delete User Session`() = runTest {
        viewModel.logout()
        verify(repository, times(1)).deleteUserSession()
    }

    @Test
    fun `when Get User, should return User Entity`() = runTest {
        whenever(repository.getUser(UserDataDummy.authModel.email))
            .thenReturn(flowOf(UserDataDummy.userEntity))
        viewModel.getUser(UserDataDummy.authModel.email)
        val actual = viewModel.uiState.getOrAwaitValue()

        actual.user shouldNotBe null
        actual.user shouldBeEqualTo UserDataDummy.userEntity
        actual.user?.email shouldBeEqualTo UserDataDummy.userEntity.email
        actual.user?.username shouldBeEqualTo UserDataDummy.userEntity.username
    }

    @Test
    fun `when Update User success, should return 1`() = runTest {
        whenever(repository.updateUser(UserDataDummy.userEntity))
            .thenReturn(Result.Success(1))
        viewModel.updateUser(UserDataDummy.userEntity)
        val actual = viewModel.uiState.getOrAwaitValue()

        actual shouldNotBe null
        actual.result shouldBeEqualTo 1
    }

    @Test
    fun `when Update User failed, should throw Exception`() = runTest {
        whenever(repository.updateUser(UserDataDummy.userEntity))
            .thenReturn(Result.Error(RuntimeException("Update Failed")))
        viewModel.updateUser(UserDataDummy.userEntity)
        val actual = viewModel.uiState.getOrAwaitValue()

        actual shouldNotBe null
        actual.result shouldBeEqualTo 0
        actual.errorMessage shouldBeEqualTo "Update Failed"
    }

    @Test
    fun `when Update Profile, profileUri should be Updated`() {
        viewModel.updateProfilePic("uri")
        val actual = viewModel.uiState.getOrAwaitValue()

        actual.picture shouldNotBe null
        actual.picture shouldBeEqualTo "uri"
    }

}