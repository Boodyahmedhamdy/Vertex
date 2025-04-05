import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Kotlin serialization plugin for type safe routes and navigation arguments
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.devtools.ksp")

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

secrets {
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}

android {
    namespace = "com.iti.vertex"
    compileSdk = 35

    val localProperties = rootProject.file("local.properties")
    val properties = Properties()
    properties.load(localProperties.inputStream())

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.iti.vertex"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"${properties["API_KEY"]}\"")
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
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.play.services.location)
    implementation(libs.androidx.appcompat)
    implementation(libs.places)
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")

    val mockkVersion = "1.13.17"
    testImplementation ("io.mockk:mockk-android:${mockkVersion}")
    testImplementation ("io.mockk:mockk-agent:${mockkVersion}")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // navigation
    val nav_version = "2.8.9"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    // JSON serialization library, works with the Kotlin serialization plugin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // gson
    implementation ("com.google.code.gson:gson:2.12.1")

    // glide image for compose
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    // room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    // testing room
    testImplementation("androidx.room:room-testing:$room_version")

    // data store
    implementation("androidx.datastore:datastore-preferences:1.1.4")


    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // maps compose
    implementation ("com.google.maps.android:maps-compose:6.5.2")

    // auto complete
    implementation("com.google.maps.android:places-compose:0.1.3")

    val work_version = "2.10.0"
    implementation("androidx.work:work-runtime-ktx:$work_version")


}