package com.wiryadev.binar_movie.data.preference

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthPreference : AuthPreference {

    private var authModel = AuthModel("", "")

    override fun getUserSession(): Flow<AuthModel> {
        return flowOf(authModel)
    }

    override suspend fun saveUserSession(user: AuthModel) {
        authModel = user
    }

    override suspend fun deleteUserSession() {
        authModel = AuthModel("", "")
    }

}