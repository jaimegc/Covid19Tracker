plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("name.remal.check-dependency-updates")
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.3"

    defaultConfig {
        applicationId = "com.jaimegc.covid19tracker"
        minSdkVersion(29)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    viewBinding {
       android.buildFeatures.viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.72")

    google()
    square()
    koin()
    other()
    test()
}
