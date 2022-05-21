package com.wiryadev.binar_movie.data.preference

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferenceDataSourceImpl @Inject constructor(
    private val authPreference: AuthPreference
) : UserPreferenceDataSource {

    override fun getUserSession(): Flow<AuthModel> {
        return authPreference.getUserSession()
    }

    override suspend fun saveUserSession(user: AuthModel) {
        authPreference.saveUserSession(user = user)
    }

    override suspend fun deleteUserSession() {
        authPreference.deleteUserSession()
    }

}