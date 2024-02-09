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
  if(!isCI) {
    jvm {
      jvmToolchain(11)
    }
//    js {
//      browser()
//      nodejs()
//    }

    linuxX64()
//    macosX64()
//    mingwX64()
  }

  // If we are a CI, build all the targets for the specified platform
  if (isLinux && isCI) {
    jvm {
      jvmToolchain(11)
    }
//    js {
//      browser()
//      nodejs()
//    }

    linuxX64()
    linuxArm64()

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmWasi()
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs()

    androidNativeX64()
    androidNativeX86()
    androidNativeArm32()
    androidNativeArm64()

    // Linux needs to know about these
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
//    tvosX64()
//    tvosArm64()
//    watchosX64()
//    watchosArm32()
//    watchosArm64()

    mingwX64()
  }

  if (isMac && isCI) {
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
//    tvosX64()
//    tvosArm64()
//    watchosX64()
//    watchosArm32()
//    watchosArm64()
  }
  if (isWindows && isCI) {
    mingwX64()
  }

  sourceSets {
    val commonMain by getting {
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
  add("kspCommonMainMetadata", project(":decomat-ksp"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  if (name != "kspCommonMainKotlinMetadata") {
    dependsOn("kspCommonMainKotlinMetadata")
  }
}

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
