import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.leishmaniapp.analysis.lam"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.leishmaniapp.analysis.lam.debugger"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            kotlinOptions {
                freeCompilerArgs = listOf("-Xdebug")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {

    /* Kotlin Core */
    implementation(platform(libs.kotlin.bom))

    /* Kotlinx */
    implementation(libs.bundles.kotlinx)

    /* Leishmaniapp */
    implementation(libs.bundles.leishmaniapp)

    /* Androidx Core */
    implementation(libs.bundles.androidx.core)

    /* Androidx Hilt */
    implementation(libs.bundles.androidx.hilt)

    /* Androidx Lifecycle */
    implementation(libs.bundles.androidx.lifecycle)

    /* Androidx Compose */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.bundles.androidx.compose.debug)

    /* Kotlin Symbol Processing */
    ksp(libs.bundles.ksp)
}