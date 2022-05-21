package com.wiryadev.binar_movie.data.repositories.user

import com.wiryadev.binar_movie.data.local.UserLocalDataSource
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.data.preference.AuthModel
import com.wiryadev.binar_movie.data.preference.UserPreferenceDataStore
import com.wiryadev.binar_movie.data.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val preferenceDataStore: UserPreferenceDataStore,
) : UserRepository {

    override suspend fun register(user: UserEntity) {
        localDataSource.register(user = user)
    }

    override suspend fun login(email: String, password: String): Flow<UserEntity> {
        return localDataSource.login(
            email = email,
            password = password,
        )
    }

    override suspend fun checkUserExist(email: String): Int {
        return localDataSource.checkUserExist(email = email)
    }

    override suspend fun getUserSession(): Flow<AuthModel> {
        return preferenceDataStore.getUserSession()
    }

    override suspend fun saveUserSession(user: AuthModel) {
        preferenceDataStore.saveUserSession(user = user)
    }

    override suspend fun deleteUserSession() {
        preferenceDataStore.deleteUserSession()
    }

    override fun getUser(email: String): Flow<UserEntity> {
        return localDataSource.getUser(email = email)
    }

    override suspend fun updateUser(user: UserEntity): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                val result = localDataSource.updateUser(user = user)
                Result.Success(result)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

}