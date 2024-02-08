import dev.anies.gradle.template.TemplateTask
import java.io.OutputStreamWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import freemarker.template.TemplateDirectiveModel
import freemarker.template.TemplateException
import freemarker.core.Environment
import freemarker.template.Configuration
import freemarker.template.TemplateDirectiveBody
import freemarker.template.TemplateModel
import freemarker.template.SimpleScalar
import freemarker.template.Template
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.internal.impldep.org.apache.commons.io.output.ByteArrayOutputStream
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import java.io.Writer
import java.nio.charset.Charset

plugins {
    kotlin("multiplatform")
    id("dev.anies.gradle.template")
    signing
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

  jvm {
    jvmToolchain(11)
  }

  if(!isCI) {
    js {
      browser()
      nodejs()
    }

    linuxX64()
    macosX64()
    mingwX64()
  }

  // If we are a CI, build all the targets for the specified platform
  if (isLinux && isCI) {
    js {
      browser()
      nodejs()
    }

    linuxX64()
    linuxArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmWasi()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs()

    androidNativeX64()
    androidNativeX86()
    androidNativeArm32()
    androidNativeArm64()

    // Need to know about this since we publish the -tooling metadata from
    // the linux containers. Although it doesn't build these it needs to know about them.
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    tvosX64()
    tvosArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()

    mingwX64()
  }

  if (isMac && isCI) {
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    tvosX64()
    tvosArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
  }
  if (isWindows && isCI) {
    mingwX64()
  }


    sourceSets {
        commonMain {
            kotlin.srcDir("$buildDir/templates/")
            dependencies {
            }
        }

        commonTest {
            kotlin.srcDir("$buildDir/templates/")
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

tasks.withType<AbstractTestTask>().configureEach {
    testLogging {
        showStandardStreams = true
        showExceptions = true
        exceptionFormat = TestExceptionFormat.SHORT
        events(TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
}

tasks.register<TemplateTask>("template_base", TemplateTask::class) {
    data = mutableMapOf("key" to "value")
    from("src/templates/")
    into("build/templates/io/decomat")
}

val runFreemarkerTemplate by tasks.registering {
    doLast {
        val cfg = Configuration(Configuration.VERSION_2_3_0)
        cfg.setDefaultEncoding("UTF-8")

        // if 'decomat-core/src/templates/' output doesn't exist, create it
        val created = File(projectDir, "build/templates/io/decomat").mkdirs()
        //println("----- Creating dirs: build/templates/io/decomat: ${created} ------")

        //println("----- Creating Template -----")
        val template = Template("", File(projectDir, "src/templates/Pattern3.ftl").readText(), cfg)
        val root =
            HashMap<String, Any>()
                .apply {
                    put("output", OutputDirective())
                    //put("model", model.encode())
                }

        //println("----- Creating Sink: build/templates/Pattern3.ftl.gen -----")
        val file = File(projectDir, "build/templates/Pattern3.ftl.gen")
        file.createNewFile()
        val os = file.outputStream()

        val out: Writer = OutputStreamWriter(os)

        //println("----- Executing Template -----")
        template.process(root, out)
        out.flush()
        os.close()
    }
}


tasks.withType<KotlinCompile> {
    dependsOn(runFreemarkerTemplate)
}

tasks.withType<KotlinNativeCompile> {
    dependsOn(runFreemarkerTemplate)
}

// THIS is the actual task used by the KMP multiplatform plugin. Without this,
// the freemarker dependency won't run in time for the dependencies to pick it up.
// That means that a command like `./gradlew clean build` for the base-project will fail.
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
    dependsOn(runFreemarkerTemplate)
}

class OutputDirective : TemplateDirectiveModel {
    @Throws(TemplateException::class, IOException::class)
    override fun execute(
        env: Environment,
        params: Map<*, *>,
        loopVars: Array<freemarker.template.TemplateModel>,
        body: TemplateDirectiveBody
    ) {
        val file: SimpleScalar? = params["file"] as SimpleScalar?
        val fw = FileWriter(File(file.toString()))
        body.render(fw)
        fw.flush()
    }
}
