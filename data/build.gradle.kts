plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleHilt)
    alias(libs.plugins.googleKsp)
}

android {
    namespace = "com.syfttny.watchmytank.data"
    compileSdk = 34

    flavorDimensions += "environment"

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    // AndroidX & Core KTX (needed for Context, etc.)
    implementation(libs.androidx.core.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Room (Local Persistence)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    // DI - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)

    // Firebase Firestore
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    // JSON Parsing (for TypeConverters)
    implementation(libs.gson)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    // testImplementation(libs.mockito.core) // Mocking for repository tests
    // testImplementation(libs.androidx.room.testing) // Room testing utilities
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
} 