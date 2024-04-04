import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  kotlin("jvm")
  `java-library`
  `maven-publish`
  `signing`
  id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
  id("org.jetbrains.dokka") version "1.8.10"
}

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
  }
}

apply(plugin = "io.github.gradle-nexus.publish-plugin")

allprojects {

  group = "io.exoquery"
  version = "0.1.218"

  apply(plugin = "kotlin")
  apply(plugin = "maven-publish")

  repositories {
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://jitpack.io")
  }

  tasks {
    compileKotlin {
      kotlinOptions.suppressWarnings = true
      kotlinOptions.jvmTarget = "1.8"
    }

    compileJava {
      sourceCompatibility = "1.8"
      targetCompatibility = "1.8"
    }

    compileTestKotlin {
      kotlinOptions.jvmTarget = "1.8"
    }

    compileTestJava {
      targetCompatibility
    }
  }

  java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  // Can also do this instead of above, for more details
  // see here: https://stackoverflow.com/questions/69079963/how-to-set-compilejava-task-11-and-compilekotlin-task-1-8-jvm-target-com
  //  java {
  //    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
  //  }

  // Disable publishing for decomat examples
  tasks.withType<PublishToMavenRepository>().configureEach {
    onlyIf {
      publication.artifactId != "decomat-examples"
    }
  }
}

//publishing {
//  publications {
//    create<MavenPublication>("mavenJava") {
//      from(components["kotlin"])
//
//      groupId = "io.exoquery"
//      artifactId = "decomat-core"
//      version = "0.0.3"
//    }
//  }
//  repositories {
//    mavenLocal()
//  }
//}


subprojects {
  val varintName = project.name

  apply {
    plugin("org.jetbrains.kotlin.jvm")
    plugin("org.jetbrains.dokka")
  }

  val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

  tasks {
    val javadocJar by creating(Jar::class) {
      dependsOn(dokkaHtml)
      archiveClassifier.set("javadoc")
      from(dokkaHtml.outputDirectory)
    }
    val sourcesJar by creating(Jar::class) {
      archiveClassifier.set("sources")
      from(sourceSets["main"].allSource)
    }
  }

  /*
  Cannot use `publications.withType<MavenPublication> { ... } ` approach using kotlin-jvm unlike KMP
  it seems that in KMP the publication is is already created internally and you just have to configure it.
   */
  publishing {
    publications {
      create<MavenPublication>("mavenJava") {
        from(components["kotlin"])
        artifactId = varintName

        artifact(tasks["javadocJar"])
        artifact(tasks["sourcesJar"])

        pom {
          name.set("decomat")
          description.set("DecoMat - Deconstructive Pattern Matching for Kotlin")
          url.set("https://github.com/exoquery/decomat")

          licenses {
            license {
              name.set("The Apache Software License, Version 2.0")
              url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
              distribution.set("repo")
            }
          }

          developers {
            developer {
              name.set("Alexander Ioffe")
              email.set("deusaquilus@gmail.com")
              organization.set("github")
              organizationUrl.set("http://www.github.com")
            }
          }

          scm {
            url.set("https://github.com/exoquery/decomat/tree/main")
            connection.set("scm:git:git://github.com/ExoQuery/DecoMat.git")
            developerConnection.set("scm:git:ssh://github.com:ExoQuery/DecoMat.git")
          }
        }
      }
    }
  }

  tasks.withType<Sign> {
    onlyIf { !project.hasProperty("nosign") }
  }

  // Check the 'skipSigning' project property
  if (!project.hasProperty("nosign")) {
    // If 'skipSigning' is not present, apply the signing plugin and configure it
    apply(plugin = "signing")

    signing {
      // use the properties passed as command line args
      // -Psigning.keyId=${{secrets.SIGNING_KEY_ID}} -Psigning.password=${{secrets.SIGNING_PASSWORD}} -Psigning.secretKeyRingFile=$(echo ~/.gradle/secring.gpg)
      sign(publishing.publications["mavenJava"])
    }
  }
}
