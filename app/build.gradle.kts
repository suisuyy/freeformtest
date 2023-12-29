plugins {
    id("com.android.application")
}

android {
    namespace = "com.android.multiwindowplayground"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.multiwindowplayground"
        minSdk = 24
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {


    testImplementation("junit:junit:4.13.2")
    implementation("com.android.support:appcompat-v7:27.0.0")
   implementation("com.android.support:support-annotations:27.0.0")
    implementation("com.android.support.test.espresso:espresso-core:2.2.1")
}