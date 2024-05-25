plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("org.jetbrains.kotlin.plugin.serialization") version("1.8.10")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.classroom"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.classroom"
        minSdk = 24
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    /** Dependencias Viewmodel*/
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    //Dependencias de Navigation
    implementation ("androidx.navigation:navigation-compose:2.5.3")

    //Room
    implementation ("androidx.room:room-runtime:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")

    //data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // ktor
    //implementation 'io.ktor:ktor-client-android:2.3.0'
    implementation ("io.ktor:ktor-client-okhttp:2.3.0")
    implementation ("io.ktor:ktor-client-serialization:2.3.0")
    implementation ("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
    implementation ("io.ktor:ktor-client-content-negotiation:2.3.0")
    implementation ("io.ktor:ktor-client-logging-jvm:2.3.0")
    implementation("io.ktor:ktor-client-core:2.3.0")

    implementation ("com.google.code.gson:gson:2.8.8")

    //Video player
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

    //splash
    implementation ("androidx.core:core-splashscreen:1.0.0-alpha02")

    //timber
    implementation ("com.jakewharton.timber:timber:4.7.1")

    //More Icons
    implementation ("androidx.compose.material:material-icons-extended:1.3.1")

    //pull refresh
    implementation ("androidx.compose.material:material:1.4.0-beta01")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-beta01")

    //lottie
    implementation ("com.airbnb.android:lottie-compose:6.1.0")

    //system ui controller (Cambiar color status bar y navigation buttons)
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
}