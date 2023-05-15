import dev.anies.gradle.template.TemplateTask

plugins {
    kotlin("jvm")
    application
    //id("com.google.devtools.ksp") version "1.8.20"
    id("maven-publish")
    idea
    id("dev.anies.gradle.template")
}

sourceSets["main"].kotlin.srcDir(file("build/templates"))

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


tasks.register<TemplateTask>("template_base", TemplateTask::class) {
    data = mutableMapOf("key" to "value")
    from("src/templates/")
    into("build/templates/io/decomat")
}

tasks.register("template", Sync::class) {
    dependsOn("template_base")
    from("build/templates/io/decomat")
    into("build/templates/io/decomat")
    include("*.ftl")
    rename { filename: String ->
        filename.replace(".ftl", ".kt")
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
