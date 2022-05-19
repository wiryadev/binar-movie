package com.wiryadev.binar_movie.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : AuthPreference {

    override fun getUserSession(): Flow<AuthModel> = dataStore.data.map { pref ->
        AuthModel(
            username = pref[USERNAME_KEY] ?: "",
            email = pref[EMAIL_KEY] ?: "",
        )
    }

    override suspend fun saveUserSession(user: AuthModel) {
        dataStore.edit { pref ->
            pref[USERNAME_KEY] = user.username
            pref[EMAIL_KEY] = user.email
        }
    }

    override suspend fun deleteUserSession() {
        dataStore.edit { pref ->
            pref[USERNAME_KEY] = ""
            pref[EMAIL_KEY] = ""
        }
    }

}

private val USERNAME_KEY = stringPreferencesKey("username")
private val EMAIL_KEY = stringPreferencesKey("email")