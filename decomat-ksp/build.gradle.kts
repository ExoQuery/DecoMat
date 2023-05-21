plugins {
  kotlin("jvm")
  application
  id("com.google.devtools.ksp")
  id("maven-publish")
  idea
  signing
}

dependencies {
  project(":decomat-core")
  implementation("com.google.devtools.ksp:symbol-processing-api:1.8.20-1.0.11")
  implementation(kotlin("stdlib-jdk8"))
  testImplementation(kotlin("test"))
  //implementation("com.facebook:ktfmt:0.43")
  implementation(kotlin("reflect"))
}
