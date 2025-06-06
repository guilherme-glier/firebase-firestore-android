plugins {
    id("com.google.gms.google-services")
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.firebasefirestore"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.firebasefirestore"
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
}

dependencies {

    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation(libs.firebase.analytics)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}