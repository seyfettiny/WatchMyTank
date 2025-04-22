plugins {
    alias(libs.plugins.jetbrainsKotlinJvm) // Use kotlin("jvm") instead of kotlin("android")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // implementation(libs.hilt.core)
    implementation(libs.javax.inject)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    //testImplementation(libs.mockito.core)
}