package com.wiryadev.binar_movie.data.repositories.user

import com.wiryadev.binar_movie.data.local.FakeUserLocalDataSource
import com.wiryadev.binar_movie.data.local.UserLocalDataSource
import com.wiryadev.binar_movie.data.preference.FakeUserPreferenceDataSource
import com.wiryadev.binar_movie.data.preference.UserPreferenceDataSource
import com.wiryadev.binar_movie.data.remote.Result
import com.wiryadev.binar_movie.utils.MainCoroutineRule
import com.wiryadev.binar_movie.utils.UserDataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.coInvoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    private val localDataSource: UserLocalDataSource = FakeUserLocalDataSource()
    private val preferenceDataSource: UserPreferenceDataSource = FakeUserPreferenceDataSource()

    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(
            localDataSource = localDataSource,
            preferenceDataSource = preferenceDataSource
        )
    }

    @Test
    fun `when Register and check, new user should be found`() = runTest {
        val expected = UserDataDummy.generateUser("binar")
        launch { repository.register(expected) }
        advanceUntilIdle()

        val actual = repository.getUser("binar@mail.com").first()
        actual shouldBeEqualTo expected
        actual.username shouldBeEqualTo expected.username
        actual.email shouldBeEqualTo expected.email
    }

    @Test
    fun `when Login using existing data, existing user should be found`() = runTest {
        val expected = UserDataDummy.userEntity
        val actual = repository.login(expected.email, expected.password).first()

        actual shouldBeEqualTo expected
        actual.username shouldBeEqualTo expected.username
        actual.email shouldBeEqualTo expected.email
    }

    @Test
    fun `when Login using non-exist data, should throw Exception`() = runTest {
        coInvoking {
            repository.login("", "")
        } shouldThrow RuntimeException::class
    }

    @Test
    fun `when Check User that exists, should return 1`() = runTest {
        val actual = repository.checkUserExist(UserDataDummy.userEntity.email)

        actual shouldBeEqualTo 1
    }

    @Test
    fun `when Check User that non-exist, should return 0`() = runTest {
        val actual = repository.checkUserExist("")

        actual shouldBeEqualTo 0
    }

    @Test
    fun `when Get Session without any authenticated user, should return Empty AuthModel`() =
        runTest {
            val actual = repository.getUserSession().first()

            actual.email shouldBeEqualTo ""
            actual.username shouldBeEqualTo ""
        }

    @Test
    fun `when Save User Session and then check, should return expected AuthModel`() = runTest {
        val expected = UserDataDummy.authModel
        launch { repository.saveUserSession(expected) }
        advanceUntilIdle()

        val actual = repository.getUserSession().first()
        actual.email shouldBeEqualTo expected.email
        actual.username shouldBeEqualTo expected.username
    }

    @Test
    fun `when Delete User Session and then check, should return Empty AuthModel`() = runTest {
        val expected = UserDataDummy.authModel
        launch { repository.saveUserSession(expected) }
        launch { repository.deleteUserSession() }
        advanceUntilIdle()

        val actual = repository.getUserSession().first()
        actual.email shouldBeEqualTo ""
        actual.username shouldBeEqualTo ""
    }

    @Test
    fun `when Get User success, should return User`() = runTest {
        val expected = UserDataDummy.userEntity
        val actual = repository.getUser(expected.email).first()

        actual shouldBeEqualTo expected
        actual.email shouldBeEqualTo expected.email
        actual.username shouldBeEqualTo expected.username
    }

    @Test
    fun `when Get User failed, should throw Exception`() = runTest {
        coInvoking {
            repository.getUser("")
        } shouldThrow RuntimeException::class
    }

    @Test
    fun `when Update User success, should return 1`() = runTest {
        val updatedUser = UserDataDummy.userEntity.copy(
            password = "newPassword"
        )
        val expected = Result.Success(1)
        val actual = repository.updateUser(updatedUser)

        actual shouldBeEqualTo expected
    }

    @Test
    fun `when Update User not found, should return 0`() = runTest {
        val updatedUser = UserDataDummy.userEntity.copy(
            email = "some random string"
        )
        val expected = Result.Success(0)
        val actual = repository.updateUser(updatedUser)

        actual shouldBeEqualTo expected
    }

}