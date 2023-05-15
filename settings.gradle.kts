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



dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://jitpack.io")
    maven(url = "https://dl.bintray.com/arrow-kt/arrow-kt/")
  }
}

rootProject.name = "decomat"

include(":decomat-core")
