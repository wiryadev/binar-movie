package com.wiryadev.binar_movie.data.local

import com.wiryadev.binar_movie.data.local.db.UserDao
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
) : UserLocalDataSource {

    override suspend fun register(user: UserEntity) {
        userDao.register(user = user)
    }

    override fun login(email: String, password: String): Flow<UserEntity> {
        return userDao.login(
            email = email,
            password = password,
        )
    }

    override fun checkUserExist(email: String): Int {
        return userDao.checkUserExist(email = email)
    }

    override fun getUser(email: String): Flow<UserEntity> {
        return userDao.getUser(email = email)
    }

    override suspend fun updateUser(user: UserEntity): Int {
        return userDao.updateUser(user = user)
    }

}