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
    }

    object Gradle {
        const val GRADLE_ANDROID = "4.0.0"
        const val KOTLIN = "1.3.72"
        const val MAVEN_PLUGIN = "2.1"
        const val GOOGLE_SERVICES = "4.3.3"
        const val REMAL_PLUGIN = "1.0.194"
    }

    object Kotlin {
        const val JDK = Gradle.KOTLIN
    }

    object Google {
        object Androidx {
            const val APP_COMPAT = "1.1.0"
            const val CONSTRAINT_LAYOUT = "2.0.0-beta5"
            const val CORE_KTX = "1.3.0"
            const val ESPRESSO = "3.2.0"
            const val JUNIT_EXT = "1.1.1"
            const val LIFECYCLE = "2.2.0"
            const val NAVIGATION = "2.2.2"
            const val RECYCLERVIEW = "1.2.0-alpha03"
            const val ROOM = "2.2.5"
            const val WORK_MANAGER = "2.3.4"
        }

        object Material {
            const val DESIGN = "1.1.0"
        }
    }

    object Square {
        const val MOSHI = "1.9.2"
        const val OK_HTTP = "4.7.2"
        const val RETROFIT = "2.9.0"
        const val RETROFIT_CONVERTER_MOSHI = "2.9.0"
    }

    object Koin {
        const val KOIN = "2.1.6"
    }

    object Test {
        const val JUNIT = "4.13"
    }

    object Detekt {
        const val DETEKT = "1.10.0-RC1"
    }

    object Arrow {
        const val ARROW = "0.10.5"
    }

    object Other {
        const val AIRBNB_LOTTIE = "3.4.1"
        const val CHART = "v3.1.0"
        const val EMOJI = "5.1.1"
    }
}