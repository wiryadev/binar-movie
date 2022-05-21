package com.wiryadev.binar_movie.data.preference

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferenceDataStoreImpl @Inject constructor(
    private val authPreference: AuthPreference
) : UserPreferenceDataStore {

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