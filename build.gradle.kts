import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Base64

plugins {
  `maven-publish`
  signing
  kotlin("multiplatform") version "2.2.0" apply false
  id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
  id("org.jetbrains.dokka") version "1.9.20"
}

allprojects {
  group = "io.exoquery"
  version = "1.0.0"

  //val varintName = project.name

  apply {
    plugin("org.jetbrains.dokka")
    plugin("maven-publish")
    plugin("signing")
  }

  repositories {
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://jitpack.io")
  }

  val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
  val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
  }

  publishing {
    val user = System.getenv("SONATYPE_USERNAME")
    val pass = System.getenv("SONATYPE_PASSWORD")

    repositories {
      maven {
        name = "Oss"
        setUrl {
          "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
        }
        credentials {
          username = user
          password = pass
        }
      }
      maven {
        name = "Snapshot"
        setUrl { "https://central.sonatype.com/repository/maven-snapshots/" }
        credentials {
          username = user
          password = pass
        }
      }
    }

    publications.withType<MavenPublication> {
      artifact(javadocJar)

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

  val isCI = project.hasProperty("isCI")
  val isLocal = !isCI
  val noSign = project.hasProperty("nosign")
  val doNotSign = isLocal || noSign

  signing {
    // Sign if we're not doing a local build and we haven't specifically disabled it
    if (!doNotSign) {
      val signingKeyRaw = System.getenv("NEW_SIGNING_KEY_ID_BASE64")
      if (signingKeyRaw == null) error("ERROR: No Signing Key Found")
      // Seems like the right way was to have newlines after all the exported (ascii armored) lines
      // and you can put them into the github-var with newlines but if you
      // include the "-----BEGIN PGP PRIVATE KEY BLOCK-----" and "-----END PGP PRIVATE KEY BLOCK-----"
      // parts with that then errors happen. Have a look at https://github.com/gradle/gradle/issues/15718 for more detail
      // Ultimately however `iurysza` is only partially correct and they key-itself does not need to be escaped
      // and can be put into a github-var with newlines.
      val signingKey =
        "-----BEGIN PGP PRIVATE KEY BLOCK-----\n\n${signingKeyRaw}\n-----END PGP PRIVATE KEY BLOCK-----"
      useInMemoryPgpKeys(
        System.getenv("NEW_SIGNING_KEY_ID_BASE64_ID"),
        signingKey,
        System.getenv("NEW_SIGNING_KEY_ID_BASE64_PASS")
      )
      sign(publishing.publications)
    }
  }

  // Fix for Kotlin issue: https://youtrack.jetbrains.com/issue/KT-61313
  tasks.withType<Sign>().configureEach {
    val pubName = name.removePrefix("sign").removeSuffix("Publication")

    // These tasks only exist for native targets, hence findByName() to avoid trying to find them for other targets

    // Task ':linkDebugTest<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
    tasks.findByName("linkDebugTest$pubName")?.let {
      mustRunAfter(it)
    }
    // Task ':compileTestKotlin<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
    tasks.findByName("compileTestKotlin$pubName")?.let {
      mustRunAfter(it)
    }
  }

  // Was having odd issues happening in CI releases like this:
  // e.g. Task ':pprint-kotlin-core:publish<AndroidNativeArm32>PublicationToOssRepository' uses this output of task ':pprint-kotlin-core:sign<AndroidNativeArm64>Publication' without declaring an explicit or implicit dependency.
  // I tried a few things that caused other issues. Ultimately the working solution I got from here:
  // https://github.com/gradle/gradle/issues/26091#issuecomment-1722947958
  tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)

    // Also, do not publish the decomat-examples project
    onlyIf {
      !this.project.name.contains("decomat-examples")
    }
  }
}


tasks.register("publishLinuxLocal") {
  dependsOn(
    ":${Release.Project.`decomat-core`}:publishToMavenLocal",
    ":${Release.Project.`decomat-ksp`}:publishToMavenLocal"
  )
}


object Release {

  object Project {
    val `decomat-core` = "decomat-core"
    val `decomat-ksp` = "decomat-ksp"
  }

  val macBuildCommands =
    listOf(
      "iosX64",
      "iosArm64",
      "tvosX64",
      "tvosArm64",
      "watchosX64",
      "watchosArm32",
      "watchosArm64",
      "macosX64",
      "macosArm64",
      "iosSimulatorArm64"
    ).map { "publish${it.capitalize()}PublicationToOssRepository" }

  val windowsBuildCommands =
    listOf(
      "mingwX64"
    ).map { "publish${it.capitalize()}PublicationToOssRepository" }

  fun String.capitalize() = this.replaceFirstChar { it.uppercase() }
}




data class Repo(
  val key: String,
  val state: String,
  val description: String? = null,
  val portal_deployment_id: String? = null
) {
  val encodedKey get() = java.net.URLEncoder.encode(key, java.nio.charset.StandardCharsets.UTF_8)
  val showName get() = description?.let { "${it}-${key}" } ?: key
}

data class Wrapper(val repositories: List<Repo>) {
  val repositoriesSorted = repositories.sortedBy { it.showName }
}

fun HttpClient.listStagingRepos(user: String, pass: String): Wrapper {
  val pid   = "io.exoquery"
  val auth     = Base64.getEncoder().encodeToString("$user:$pass".toByteArray())
  val request  = HttpRequest.newBuilder()
    .uri(URI.create("https://ossrh-staging-api.central.sonatype.com/manual/search/repositories?profile_id=$pid&ip=any"))
    .header("Content-Type", "application/json")
    .header("Authorization", "Basic $auth")
    .GET()
    .build()

  val mapper = jacksonObjectMapper()

  fun tryPrintJson(json: String) {
    try {
      mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(json))
    } catch (e: Exception) {
      json
    }
  }
  val response = this.send(request, HttpResponse.BodyHandlers.ofString())
  println("================ /manual/search/repositories Response Code: ${response.statusCode()}: ================\n${tryPrintJson(response.body())}")

  /* 1.  Sanity-check the HTTP call */
  if (response.statusCode() !in 200..299) {
    val msg = "================ OSS RH search failed Code:${response.statusCode()} ================\n${response.body()}"
    logger.error(msg)
    throw GradleException("Search request was not successful because of:\n${msg}")
  }

  val payload: Wrapper = mapper.readValue<Wrapper>(response.body())
  return payload
}

val publishSonatypeStaging by tasks.registering {
  description = "Creates a new OSSRH staging repository and records its ID"

  doLast {
    /* ---- gather inputs exactly as before ---- */
    val user  = System.getenv("SONATYPE_USERNAME")   ?: error("SONATYPE_USERNAME not set")
    val pass  = System.getenv("SONATYPE_PASSWORD")   ?: error("SONATYPE_PASSWORD not set")
    val http = HttpClient.newHttpClient()
    val auth = Base64.getEncoder().encodeToString("$user:$pass".toByteArray())

    /* Pick the repositories that were not uploaded yet */
    val matching = http.listStagingRepos(user, pass).repositoriesSorted.filter { it.portal_deployment_id == null }

    if (matching.isEmpty()) {
      logger.lifecycle("No unpublished repositories found.")
      return@doLast
    } else {
      println("---------------- Found ${matching.size} unpublished repositories ---------------\n${matching.joinToString("\n")}")
    }

    var ok = 0
    var failed = 0

    matching.forEach { repo ->
      println("==== Processing Repo: ${repo.showName} ====")

      // Encode the key exactly like `jq -sRr @uri`
      val enc = repo.encodedKey
      val promoteRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://ossrh-staging-api.central.sonatype.com/manual/upload/repository/$enc?publishing_type=user_managed"))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .header("Authorization", "Basic $auth")
        .POST(HttpRequest.BodyPublishers.ofString("{}"))
        .build()

      val promoteResp = http.send(promoteRequest, HttpResponse.BodyHandlers.ofString())
      if (promoteResp.statusCode() in 200..299) {
        println("--- Promoted staging repo ${repo.showName} ---")
        ok++
      } else {
        println("--- Failed to promote repo ${repo.showName} - HTTP Code ${promoteResp.statusCode()} ---\n${promoteResp.body()}")
        failed++
      }
    }
    println("==== Processing of Repos Completed ====")

    if (failed > 0) {
      throw GradleException("Some repositories failed to publish: $failed of ${matching.size}")
    } else {
      println("All $ok staging repositories successfully switched to user-managed.")
    }
  }
}
