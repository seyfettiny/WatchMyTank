package com.syfttny.watchmytank

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WatchMyTankApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        try {
            FirebaseApp.initializeApp(this)
        } catch (t: Throwable) {
            Log.w("WatchMyTankApp", "Firebase init failed or already initialized: ${t.message}")
        }

        Firebase.analytics.setAnalyticsCollectionEnabled(BuildConfig.ENABLE_ANALYTICS)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(BuildConfig.ENABLE_CRASHLYTICS)

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(
            mapOf(
                "species_version" to "1",
                "enable_suggestions" to false,
                "enable_parameter_alerts" to false
            )
        )
        remoteConfig.fetchAndActivate()

        val projectId = FirebaseApp.getInstance().options.projectId ?: ""
        val isProdProject = projectId.contains("prod", ignoreCase = true)
        if (BuildConfig.ENVIRONMENT == "DEV" && isProdProject) {
            Log.e("EnvCheck", "DEV build appears to point to PROD Firebase project: $projectId")
        }
    }
} 