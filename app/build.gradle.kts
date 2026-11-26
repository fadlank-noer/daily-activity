import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // Target platform berikutnya (desktop/iOS/web) tinggal ditambahkan di blok ini;
    // UI di commonMain otomatis diwarisi.
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
        }

        androidMain.dependencies {
            implementation("androidx.core:core-ktx:1.16.0")
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
            implementation("androidx.activity:activity-compose:1.10.1")

            // Versi mengikuti resolusi BOM 2025.06.01 sebelum migrasi KMP
            implementation("androidx.compose.ui:ui-tooling-preview:1.8.3")
        }
    }
}

android {
    namespace = "com.harini.aktivitas"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.harini.aktivitas"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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

    buildFeatures {
        compose = true
    }
}
