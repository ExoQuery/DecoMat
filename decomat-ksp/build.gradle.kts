plugins {
  kotlin("jvm")
  application
  id("com.google.devtools.ksp")
  id("maven-publish")
  idea
  signing
  id("org.jetbrains.dokka")
}

dependencies {
  project(":decomat-core")
  implementation("com.google.devtools.ksp:symbol-processing-api:2.0.0-RC1-1.0.20")
  implementation(kotlin("stdlib-jdk8"))
  testImplementation(kotlin("test"))
  //implementation("com.facebook:ktfmt:0.43")
  implementation(kotlin("reflect"))
}
