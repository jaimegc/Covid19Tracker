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

plugins {
    id(Dependencies.DETEKT_PLUGIN).version(Dependencies.DETEKT)
}

dependencies {
    detektFormatting()
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

tasks.register("detektAll", io.gitlab.arturbosch.detekt.Detekt::class) {
    buildUponDefaultConfig = true
    autoCorrect = true
    parallel = true
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/detekt.yml"))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/build/**")
    exclude("**/buildSrc/**")
    exclude("**/test/**/*.kt")
    reports {
        xml.enabled = false
        html.enabled = false
        txt.enabled = false
    }
}

apply(plugin = "name.remal.check-dependency-updates")