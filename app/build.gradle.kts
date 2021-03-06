plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("name.remal.check-dependency-updates")
    // Uncomment this line and add your own google-services file to use Firebase
    // id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("de.mannodermaus.android-junit5")
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

        setProperty("archivesBaseName", Versions.Android.DefaultConfig.APPLICATION_NAME)
    }

    buildTypes {
        getByName(Versions.Android.BuildTypes.DEBUG) {
            buildConfigField(
                "String", "BASE_URL", "\"https://api.covid19tracking.narrativa.com/api/\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xinline-classes",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=org.koin.core.component.KoinApiExtension",
            "-Xopt-in=kotlin.time.ExperimentalTime"
        )
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

    sourceSets {
        "src/sharedTest/java".apply {
            getByName("test").java.srcDirs(this)
            getByName("androidTest").java.srcDirs(this)
        }
    }

    testOptions {
        unitTests {
            this.isIncludeAndroidResources = true
        }

        unitTests.all {
            // This executes all tests JUnit5 based tests for Kotest.
            // We must use JUnit4 for the rest of the tests. See:
            // https://github.com/mannodermaus/android-junit5
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    kotlin()
    google()
    firebase()
    square()
    coroutines()
    koin()
    arrow()
    others()
    test()
}
