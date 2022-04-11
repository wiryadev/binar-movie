package com.wiryadev.binar_movie.data.repositories.user

import com.wiryadev.binar_movie.data.local.db.UserDao
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.data.preference.AuthModel
import com.wiryadev.binar_movie.data.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
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

    override suspend fun updateUser(user: UserEntity): Int {
        return userDao.updateUser(user = user)
    }

}