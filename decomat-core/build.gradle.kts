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
import org.gradle.internal.impldep.org.apache.commons.io.output.ByteArrayOutputStream
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.Writer
import java.nio.charset.Charset

plugins {
    kotlin("jvm")
    //id("com.google.devtools.ksp") version "1.8.20"
    id("maven-publish")
    idea
    // TODO Needed for the freemarker dependencies despite the fact that freemarker is imported
    //      need to look into why freemarker can't be found if this plugin is removed.
    id("dev.anies.gradle.template")
    signing
    id("org.jetbrains.dokka")
}

sourceSets["main"].kotlin.srcDir(file("build/templates"))

/*
Note that the error "could not list directory ...git/DecoMat/decomat-core/src/templates
is actually misleading. The actual error will be something else in the freemarker template
but for some reason a higher level task can't list the directory of the ftl building fails.
In order to get the real error add --stacktrace to the './gradlew runFreemarkerTemplate' task
 */
tasks.register<TemplateTask>("template_base", TemplateTask::class) {
    data = mutableMapOf("key" to "value")
    from("src/templates/")
    into("build/templates/io/decomat")
}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions{
//        freeCompilerArgs = listOf("-Xno-optimize")
//    }
//}

//tasks.register("template", Sync::class) {
//    dependsOn("template_base")
//    from("build/templates/io/decomat")
//    into("build/templates/io/decomat")
//    include("*.ftl")
//    rename { filename: String ->
//        filename.replace(".ftl", ".kt")
//    }
//}

//tasks.test {
//    useJUnitPlatform()
//}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    //implementation("com.facebook:ktfmt:0.43")
    implementation(kotlin("reflect"))
    implementation("org.freemarker:freemarker:2.3.31")
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
