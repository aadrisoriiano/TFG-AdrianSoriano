plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
        id("org.jetbrains.kotlin.plugin.compose") version "2.1.20" // this version matches your Kotlin version
    id("com.google.dagger.hilt.android") version "2.56.2"
    kotlin("kapt")
    id("io.gitlab.arturbosch.detekt") version "1.23.1"

}

android {
    namespace = "com.example.silentsmart"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.silentsmart"
        minSdk = 26
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
    }
}
dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.core.ktx)
    implementation(libs.androidx.room.runtime.android)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("com.squareup:javapoet:1.13.0")

    implementation("com.google.dagger:hilt-android:2.56.2")
    kapt("com.google.dagger:hilt-android-compiler:2.56.2")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2") // Procesador de anotaciones
    implementation ("androidx.room:room-ktx:2.5.2")


    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)



}

hilt {
    enableAggregatingTask = false
}
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config = files("$rootDir/detekt-config.yml")
    source = files("$rootDir/app/src/main/java")
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
    }
}
