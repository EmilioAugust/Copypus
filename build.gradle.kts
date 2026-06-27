buildscript {
    extra.apply {
        set("room_version", "2.7.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}