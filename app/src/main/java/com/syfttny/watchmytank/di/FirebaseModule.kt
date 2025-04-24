package com.syfttny.watchmytank.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore {
        return Firebase.firestore
        // Optional: Configure settings like persistence, caching
        // val settings = firestoreSettings {
        //     isPersistenceEnabled = true
        //     cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        // }
        // firestore.firestoreSettings = settings
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalyticsInstance(): FirebaseAnalytics {
        return Firebase.analytics
    }

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfigInstance(): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig
        // Optional: Configure settings like minimum fetch interval
        // val configSettings = remoteConfigSettings {
        //     minimumFetchIntervalInSeconds = 3600 // e.g., 1 hour
        // }
        // remoteConfig.setConfigSettingsAsync(configSettings)
        // Optional: Set default values (useful for first run or if fetch fails)
        // remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        return remoteConfig
    }

    // Add providers for other Firebase services like Auth, Storage, etc. if needed
} 