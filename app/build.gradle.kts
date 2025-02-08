import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinSafeArgs)
    alias(libs.plugins.androidHilt)
    alias(libs.plugins.sonarQube)
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        htmlReport = true
        htmlOutput = file("build/reports/lint-results.html")
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

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            it.testLogging {
                events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                exceptionFormat = TestExceptionFormat.FULL
                showStandardStreams = true
            }
            it.reports.junitXml.required.set(true)
            it.reports.html.required.set(false)
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
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.constraintlayout)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.viewbinding)
    // Compose integrations
    implementation(libs.compose.activity)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.lifecycle.viewmodel)
    implementation(libs.compose.rxjava)
    // Android Studio Preview support
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
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
    androidTestImplementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.androidx.monitor)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.test.uiautomator)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.guava)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.accessibility.test.framework)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}