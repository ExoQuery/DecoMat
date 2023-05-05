plugins {
    kotlin("jvm") version "1.8.20"
    application
    //id("com.google.devtools.ksp") version "1.8.0-1.0.8"
    idea
}

group = "org.choppythelumberjack"
version = "1.0-SNAPSHOT"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions{
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven(url = "https://dl.bintray.com/arrow-kt/arrow-kt/")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    implementation("com.facebook:ktfmt:0.43")
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}


kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}