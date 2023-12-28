pluginManagement {
  plugins {
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
    kotlin("jvm") version "1.9.20"
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
