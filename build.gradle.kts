buildscript {
    repositories {
        google()
        jcenter()
        uri("https://plugins.gradle.org/m2/")
    }
    dependencies {
        gradle()
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