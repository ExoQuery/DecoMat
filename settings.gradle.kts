include(":decomat-core", ":decomat-ksp", ":decomat-examples")

pluginManagement {
  plugins {
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
    kotlin("jvm") version "1.9.22"
    id("dev.anies.gradle.template") version "0.0.2"
  }
  repositories {
    gradlePluginPortal()
    google()
  }
}

rootProject.name = "decomat"
