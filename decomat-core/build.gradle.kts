plugins {
    kotlin("jvm")
    application
    //id("com.google.devtools.ksp") version "1.8.20"
    id("maven-publish")
    idea
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])

            groupId = "io.exoquery"
            artifactId = "decomat-core"
            version = "0.0.3"
        }
    }
    repositories {
        mavenLocal()
    }
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
