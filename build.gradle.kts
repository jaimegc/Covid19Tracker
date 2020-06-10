buildscript {
    //ext.kotlin_version = "1.3.72"
    repositories {
        google()
        jcenter()
        uri("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.72")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.google.gms:google-services:4.3.2")
        classpath("name.remal:gradle-plugins:1.0.192")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

apply(plugin = "name.remal.check-dependency-updates")