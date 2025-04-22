plugins {
    alias(libs.plugins.jetbrainsKotlinJvm) // Use kotlin("jvm") instead of kotlin("android")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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