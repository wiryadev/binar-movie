package com.wiryadev.binar_movie.data.local

import com.wiryadev.binar_movie.data.local.db.UserDao
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.utils.UserDataDummy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserDao : UserDao {

    private val users = mutableListOf(
        UserDataDummy.userEntity
    )

    override suspend fun register(user: UserEntity) {
        users.add(user)
    }

    override fun login(email: String, password: String): Flow<UserEntity> {
        val found = users.find {
            it.email == email && it.password == password
        }
        if (found != null) {
            return flowOf(found)
        } else {
            throw RuntimeException("Not Found")
        }
    }

    override fun checkUserExist(email: String): Int {
        val found = users.find {
            it.email == email
        }
        return if (found != null) 1 else 0
    }

    override fun getUser(email: String): Flow<UserEntity> {
        val found = users.find {
            it.email == email
        }
        if (found != null) {
            return flowOf(found)
        } else {
            throw RuntimeException("Not Found")
        }
    }

    override suspend fun updateUser(user: UserEntity): Int {
        val found = users.find {
            it.email == user.email
        }
        found?.let {
            users.remove(it)
            users.add(user)
            return 1
        } ?: return 0
    }
}