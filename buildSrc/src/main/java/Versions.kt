object Versions {
    object Android {
        const val BUILD_TOOLS = "29.0.3"
        const val COMPILE_SDK = 29

        object DefaultConfig {
            const val APPLICATION_ID = "com.jaimegc.covid19tracker"
            const val MIN_ANDROID_SDK = 21
            const val TARGET_ANDROID_SDK = 29
            const val VERSION_CODE = 1
            const val VERSION_NAME = "1.0"
            const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
        }

        object BuildTypes {
            const val DEBUG = "debug"
        }
    }

    object Gradle {
        const val FIREBASE_CRASHLYTICS = "2.3.0"
        const val FIREBASE_PERFORMANCE_PLUGIN = "1.3.2"
        const val GRADLE_ANDROID = "4.1.0"
        const val KOTLIN = "1.4.10"
        const val MAVEN_PLUGIN = "2.1"
        const val GOOGLE_SERVICES = "4.3.4"
        const val REMAL_PLUGIN = "1.1.1"
    }

    object Kotlin {
        const val JDK = Gradle.KOTLIN
    }

    object Google {
        object Androidx {
            const val APP_COMPAT = "1.2.0"
            const val CONSTRAINT_LAYOUT = "2.0.2"
            const val CORE_KTX = "1.3.2"
            const val CORE_TESTING = "2.1.0"
            const val ESPRESSO = "3.3.0"
            const val JUNIT_EXT = "1.1.2"
            const val LIFECYCLE = "2.2.0"
            const val NAVIGATION = "2.3.1"
            const val RECYCLERVIEW = "1.2.0-alpha04"
            const val TEST_RULES = "1.3.0"
            const val TEST_RUNNER = "1.2.0"
            const val ROOM = "2.2.5"
            const val WORK_MANAGER = "2.4.0"
        }

        object Firebase {
            const val ANALYTICS = "17.6.0"
            const val CRASHLYTICS = "17.2.2"
            const val PERFORMANCE = "19.0.9"
        }

        object Material {
            const val DESIGN = "1.2.1"
        }
    }

    object Square {
        const val MOSHI = "1.11.0"
        const val OK_HTTP = "4.9.0"
        const val RETROFIT = "2.9.0"
        const val RETROFIT_CONVERTER_MOSHI = "2.9.0"
    }

    object Coroutines {
        const val CORE = "1.4.0-M1"
        const val ANDROID = "1.4.0-M1"
    }

    object Koin {
        const val KOIN = "2.1.6"
    }

    object Test {
        const val COROUTINES = "1.4.0-M1"
        const val FLOW_TEST_OBSERVER = "1.4.1"
        const val JUNIT = "4.13.1"
        const val MOCKITO_KOTLIN = "2.2.0"
    }

    object Detekt {
        const val DETEKT = "1.14.1"
        const val DETEKT_FORMATTING = "1.12.0"
    }

    object Arrow {
        const val ARROW = "0.10.5"
    }

    object Other {
        const val AIRBNB_LOTTIE = "3.4.4"
        const val CHART = "v3.1.0"
        const val EMOJI = "5.1.1"
    }
}