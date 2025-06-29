include(":decomat-core", ":decomat-ksp", ":decomat-examples")

pluginManagement {
  plugins {
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    kotlin("jvm") version "2.2.0"
    id("dev.anies.gradle.template") version "0.0.2"
  }
  repositories {
    gradlePluginPortal()
    google()
  }
}

rootProject.name = "decomat"
