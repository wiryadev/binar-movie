package com.wiryadev.binar_movie.data.local

import com.wiryadev.binar_movie.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {

    suspend fun register(user: UserEntity)

    fun login(email: String, password: String): Flow<UserEntity>

    fun checkUserExist(email: String): Int

    fun getUser(email: String): Flow<UserEntity>

    suspend fun updateUser(user: UserEntity): Int

}