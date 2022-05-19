package com.wiryadev.binar_movie.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.wiryadev.binar_movie.data.preference.AuthPreference
import com.wiryadev.binar_movie.data.preference.AuthPreferenceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val AUTH_PREFERENCES = "auth_preferences"

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create {
        appContext.preferencesDataStoreFile(AUTH_PREFERENCES)
    }

    @Singleton
    @Provides
    fun provideAuthPreference(
        dataStore: DataStore<Preferences>
    ): AuthPreference = AuthPreferenceImpl(dataStore)

}