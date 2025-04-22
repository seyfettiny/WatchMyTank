plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleHilt) // Hilt plugin
    alias(libs.plugins.googleKsp) // KSP for Room & Hilt
}

android {
    namespace = "com.syfttny.watchmytank.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Define Supabase URL and Key securely (e.g., from local.properties or build config)
        // buildConfigField "String", "SUPABASE_URL", "\"YOUR_SUPABASE_URL\""
        // buildConfigField "String", "SUPABASE_KEY", "\"YOUR_SUPABASE_ANON_KEY\""
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // Enable build features if needed (e.g., buildConfig)
    // buildFeatures {
    //     buildConfig = true
    // }
}

dependencies {
    implementation(project(":domain")) // Depends on domain interfaces/models
    implementation(project(":core")) // May need core utilities (e.g. DispatchersModule)

    // AndroidX & Core KTX (needed for Context, etc.)
    implementation(libs.androidx.core.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android) // May be needed if interacting with Main dispatcher

    // Room (Local Persistence)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Kotlin Extensions for Room (Coroutines support)
    ksp(libs.androidx.room.compiler) // Use ksp instead of kapt

    // Supabase (Remote Persistence) - Using official library
    // implementation(libs.supabase.kt.client) // Add appropriate supabase BOM/libs when known
    // implementation(libs.ktor.client.cio) // Default engine for supabase-kt


    // DI - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // Use ksp instead of kapt for Hilt

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    // testImplementation(libs.mockito.core) // Mocking for repository tests
    // testImplementation(libs.androidx.room.testing) // Room testing utilities
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Configure KSP for Room schema location
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
} 