plugins {
    kotlin("jvm")
    application
    id("com.google.devtools.ksp") version "1.8.20"
    id("maven-publish")
    idea
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


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    //implementation("com.facebook:ktfmt:0.43")
    implementation(kotlin("reflect"))
}
