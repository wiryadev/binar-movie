package com.wiryadev.binar_movie.data.repositories.user

import com.wiryadev.binar_movie.data.local.db.UserDao
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.data.preference.AuthModel
import com.wiryadev.binar_movie.data.preference.AuthPreference
import com.wiryadev.binar_movie.data.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val preference: AuthPreference,
) : UserRepository {

    override suspend fun register(user: UserEntity) {
        userDao.register(user = user)
    }

    override suspend fun login(email: String, password: String): Flow<UserEntity> {
        return userDao.login(
            email = email,
            password = password,
        )
    }

    override suspend fun checkUserExist(email: String): Int {
        return userDao.checkUserExist(email = email)
    }

    override suspend fun getUserSession(): Flow<AuthModel> {
        return preference.getUserSession()
    }

    override suspend fun saveUserSession(user: AuthModel) {
        preference.saveUserSession(user = user)
    }

    override suspend fun deleteUserSession() {
        preference.deleteUserSession()
    }

    override fun getUser(email: String): Flow<UserEntity> {
        return userDao.getUser(email = email)
    }

    override suspend fun updateUser(user: UserEntity): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                val result = userDao.updateUser(user = user)
                Result.Success(result)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

}