plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("org.sonarqube")
}

android {

    defaultConfig {
        compileSdk = 34
        applicationId = "com.elksa.sample.buscador.mercadolibre"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled  = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    sonarqube {
        properties {
            property("sonar.projectName", "meli-browser-android")
            property("sonar.projectKey", "meli-browser-android")
            property("sonar.tests", listOf("src/test/java"))
            property("sonar.test.inclusions", "**/*Test*/**")
            property("sonar.sourceEncoding", "UTF-8")
            property("sonar.sources", "src/main/java")
            property(
                    "sonar.exclusions", "**/*Test*/**," +
                    "*.json," +
                    "**/*test*/**," +
                    "**/.gradle/**," +
                    "**/R.class"
            )
        }
    }
    namespace = "com.elksa.sample.buscador.mercadolibre"
}

dependencies {
    implementation(project(":ui"))
    implementation(libs.material)
    implementation(libs.core.ktx)
    implementation(libs.app.compat)
    implementation(libs.constraintlayout)
    implementation(libs.support)
    // Lifecycle
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    // Navigation component
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    // Rx
    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter)
    // Interceptor
    implementation(libs.interceptor)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.core.testing)
    testImplementation(libs.kotlin.reflect)
    // Mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    // Instrumentation tests
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
}