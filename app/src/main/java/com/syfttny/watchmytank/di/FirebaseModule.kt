package com.syfttny.watchmytank.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
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
        return remoteConfig
    }
} 