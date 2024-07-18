import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
  kotlin("multiplatform")
  signing
  id("com.google.devtools.ksp")
}

kotlin {
  val isCI = project.hasProperty("isCI")
  val platform =
    if (project.hasProperty("platform"))
      project.property("platform")
    else
      "any"
  val isLinux = platform == "linux"
  val isMac = platform == "mac"
  val isWindows = platform == "windows"


  // If we're not the CI build a limited set of standard targets
  jvm {
    //jvmToolchain(11)
  }
    js {
      nodejs()
      //browser()
    }

  linuxX64()

  sourceSets {
    val commonMain by getting {
      println("------------------------------------------------------------------------------")
      println("------------------------------------------------------------------------------")
      println("---------------------- Build Dir List: ${File("$buildDir/generated/ksp/metadata/commonMain/kotlin").listFiles()?.let { it.map { it.name } }} ------------------------------")
      println("-----------------------Build Dir: $buildDir -----------------------------------------")
      println("------------------------------------------------------------------------------")
      println("------------------------------------------------------------------------------")

      kotlin.srcDir("$buildDir/generated/ksp/metadata/commonMain/kotlin")
      dependencies {
        //kotlin.srcDir("$buildDir/generated/ksp/main/kotlin")
        api(project(":decomat-core"))
      }
    }

    val commonTest by getting {
      kotlin.srcDir("$buildDir/generated/ksp/metadata/commonMain/kotlin")
      dependencies {
        // Used to ad-hoc some examples but not needed.
        //api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.2")
        //implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
        implementation(kotlin("test"))
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
  }
}

dependencies {
  // NOTE. This has failed before on upstream dependencies which turned out
  // to cuase errors in `compileCommonMainKotlinMetadata`. This was fixed by adding
  // `dependsOn(runFreemarkerTemplate)` specifically for `org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>`.
  // Specifically `./gradlew clean build` and `./gradlew clean;./gradlew build` would fail while
  // `./gradlew clean; ./gradlew decomat-core:build; ./gradlew build` would succeed which meant
  // that something that `decomat-core` was doing was not getting picked up by decomat-examples
  // until a subsequent build. This turned out to be the `runFreemarkerTemplate` task results.
  add("kspCommonMainMetadata", project(":decomat-ksp"))

  // Don't think this is needed
  //add("kspLinuxX64", project(":decomat-ksp"))
}

tasks.register("listTasks") {
  doLast {
    println("Available tasks:")
    tasks.forEach { task ->
      println("${task.name} - ${task::class.java}")
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
  if (name != "kspCommonMainKotlinMetadata") {
    dependsOn("kspCommonMainKotlinMetadata")
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  if (name != "kspCommonMainKotlinMetadata") {
    dependsOn("kspCommonMainKotlinMetadata")
  }
}

// THIS is the actual task used by the KMP multiplatform plugin.
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>().configureEach {
  if (name != "kspCommonMainKotlinMetadata") {
    dependsOn("kspCommonMainKotlinMetadata")
  }
}

if (project.hasProperty("platform") && project.property("platform") == "linux") {
  tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
  }
}

tasks.withType<AbstractTestTask>().configureEach {
  testLogging {
    showStandardStreams = true
    showExceptions = true
    exceptionFormat = TestExceptionFormat.FULL
    events(TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
  }
}

ksp {
  arg("measureDuration", "true")
}
