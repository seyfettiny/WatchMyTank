plugins {
    alias(libs.plugins.jetbrainsKotlinJvm) 
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