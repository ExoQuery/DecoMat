pluginManagement {
  plugins {
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
    kotlin("jvm") version "2.0.0"
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
