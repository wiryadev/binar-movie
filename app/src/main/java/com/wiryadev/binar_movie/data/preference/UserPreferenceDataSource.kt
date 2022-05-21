package com.wiryadev.binar_movie.data.preference

import kotlinx.coroutines.flow.Flow

interface UserPreferenceDataSource {

    fun getUserSession(): Flow<AuthModel>

    suspend fun saveUserSession(user: AuthModel)

    suspend fun deleteUserSession()

}