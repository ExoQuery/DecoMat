@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  kotlin("multiplatform")
  id("com.google.devtools.ksp")
  id("maven-publish")
  idea
  signing
  id("org.jetbrains.dokka")
}

kotlin {
  jvmToolchain(11)

  jvm {
  }

  sourceSets {
    jvmMain {
      kotlin.srcDir("src/main/kotlin")
      resources.srcDir("src/main/resources")

      dependencies {
        project(":decomat-core")
        implementation("com.google.devtools.ksp:symbol-processing-api:2.2.0-2.0.2")

        //implementation("com.facebook:ktfmt:0.43")
        implementation(kotlin("reflect"))
      }
    }
  }

}


//publishing {
//  val varintName = project.name
//
//  val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
//
//  tasks {
//    val sourcesJar by creating(Jar::class) {
//      archiveClassifier.set("sources")
//      from(sourceSets["main"].allSource)
//    }
//  }
//
//  publications {
//    create<MavenPublication>("mavenJava") {
//      from(components["kotlin"])
//      artifactId = varintName
//
//      artifact(tasks["sourcesJar"])
//
//      pom {
//        name.set("decomat")
//        description.set("DecoMat - Deconstructive Pattern Matching for Kotlin")
//        url.set("https://github.com/exoquery/decomat")
//
//        licenses {
//          license {
//            name.set("The Apache Software License, Version 2.0")
//            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//            distribution.set("repo")
//          }
//        }
//
//        developers {
//          developer {
//            name.set("Alexander Ioffe")
//            email.set("deusaquilus@gmail.com")
//            organization.set("github")
//            organizationUrl.set("http://www.github.com")
//          }
//        }
//
//        scm {
//          url.set("https://github.com/exoquery/decomat/tree/main")
//          connection.set("scm:git:git://github.com/ExoQuery/DecoMat.git")
//          developerConnection.set("scm:git:ssh://github.com:ExoQuery/DecoMat.git")
//        }
//      }
//    }
//  }
//}
