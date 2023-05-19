pluginManagement {
  plugins {
    id("com.google.devtools.ksp") version "1.8.20"
    kotlin("jvm") version "1.8.20"
    id("dev.anies.gradle.template") version "0.0.2"
  }
  repositories {
    gradlePluginPortal()
    google()
  }
}

rootProject.name = "decomat"

include(":decomat-core")
