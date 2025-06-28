plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.example.kushagraDemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kushagraDemo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.appModuleLibraries)
    ksp(libs.hiltAndroidCompiler)
    ksp(libs.roomksp)
    ksp(libs.hilt.ext.compiler)
    ksp(libs.hilt.work)

// Core testing
    testImplementation (libs.junit)
    testImplementation (libs.core.testing)
    testImplementation (libs.kotlinx.coroutines.test)

// Mockito
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.kotlin)
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":customViews"))
    implementation(project(":core:network"))
}