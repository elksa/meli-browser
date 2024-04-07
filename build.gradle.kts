// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.kotlinKsp) apply false
    alias(libs.plugins.kotlinSafeArgs) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.androidHilt) apply false
    alias(libs.plugins.sonarQube) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}