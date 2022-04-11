package com.wiryadev.binar_movie.data.local.db

import androidx.room.*
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: UserEntity)

    @Query("SELECT * FROM tableUser WHERE email=:email AND password=:password LIMIT 1")
    fun login(email: String, password: String): Flow<UserEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM tableUser WHERE email=:email)")
    fun checkUserExist(email: String): Int

    @Query("SELECT * FROM tableUser WHERE email=:email LIMIT 1")
    fun getUser(email: String): Flow<UserEntity>

    @Update
    suspend fun updateUser(user: UserEntity): Int

}