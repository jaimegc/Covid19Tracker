object Versions {
    object Android {
        const val MIN_ANDROID_SDK = 21
        const val TARGET_ANDROID_SDK = 29
    }

    object Gradle {
        const val GRADLE_ANDROID = "4.0.0"
        const val KOTLIN = "1.3.72"
        const val MAVEN_PLUGIN = "2.1"
        const val GOOGLE_SERVICES = "4.3.3"
        const val REMAL_PLUGIN = "1.0.192"
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
        const val MOSHI_CODEGEN = "1.8.0"
        const val MOSHI_KOTLIN = "1.9.2"
        const val OK_HTTP = "3.14.0"
        const val RETROFIT = "2.9.0"
        const val RETROFIT_CONVERTER_MOSHI = "2.7.2"
    }

    object Koin {
        const val KOIN = "2.1.5"
    }

    object Test {
        const val JUNIT = "4.13"
    }

    object Other {
        const val AIRBNB_LOTTIE = "3.4.0"
        const val ARROW = "0.10.5"
        const val CHART = "v3.1.0"
        const val EMOJI = "5.1.1"
    }
}