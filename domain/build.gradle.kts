plugins {
    alias(libs.plugins.jetbrainsKotlinJvm) // Use kotlin("jvm") instead of kotlin("android")
}

java { // Configure Java toolchain for JVM projects
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // Kotlin Standard Library (implicitly included by the plugin)
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Hilt core for potential @Inject in use cases (no Android dependency needed)
    // implementation(libs.hilt.core) // Add if you use @Inject in constructors here
    implementation(libs.javax.inject) // Standard JSR-330 annotations


    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test) // For testing coroutines
    // Add Mockito or other mocking libraries if needed for use case tests
    // testImplementation(libs.mockito.core)
} 