pluginManagement {
  plugins {
    id("com.google.devtools.ksp") version "2.0.0-Beta2-1.0.16"
    kotlin("jvm") version "2.0.0-Beta2"
    id("dev.anies.gradle.template") version "0.0.2"
  }
  repositories {
    gradlePluginPortal()
    google()
  }
}

rootProject.name = "decomat"

include(":decomat-core")
include(":decomat-ksp")
include(":decomat-examples")
