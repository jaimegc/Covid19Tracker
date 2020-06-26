plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("name.remal.check-dependency-updates")
    // Uncomment this line and add your own google-services file to use Firebase
    //id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    compileSdkVersion(Versions.Android.COMPILE_SDK)
    buildToolsVersion = Versions.Android.BUILD_TOOLS

    defaultConfig {
        applicationId = Versions.Android.DefaultConfig.APPLICATION_ID
        minSdkVersion(Versions.Android.DefaultConfig.MIN_ANDROID_SDK)
        targetSdkVersion(Versions.Android.DefaultConfig.TARGET_ANDROID_SDK)
        versionCode = Versions.Android.DefaultConfig.VERSION_CODE
        versionName = Versions.Android.DefaultConfig.VERSION_NAME

        testInstrumentationRunner = Versions.Android.DefaultConfig.TEST_INSTRUMENTATION_RUNNER
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"https://api.covid19tracking.narrativa.com/api/\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    kotlin()
    google()
    firebase()
    square()
    koin()
    arrow()
    others()
    test()
}
