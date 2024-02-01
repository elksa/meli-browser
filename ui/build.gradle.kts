plugins {
    id("com.google.devtools.ksp")
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {

    defaultConfig {
        compileSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled  = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            consumerProguardFiles("consumer-rules.pro")
        }
    }
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.elksa.sample.buscador.mercadolibre.ui"
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.app.compat)
    implementation(libs.material)
    // Glide
    api(libs.glide.core)
    ksp(libs.glide.ksp)
    // Unit tests
    androidTestImplementation(libs.junit.ext)
    // Instrumentation tests
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
}