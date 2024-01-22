import com.google.devtools.ksp.gradle.KspTask

plugins {
  kotlin("jvm")
  id("com.google.devtools.ksp")

//  application
}

// Allows running from command line using  ./gradlew :main-project:run
//application {
//  mainClass.set("com.morfly.MainKt")
//}

// Makes generated code visible to IDE
kotlin.sourceSets.main {
  kotlin.srcDirs(
    file("$buildDir/generated/ksp/main/kotlin"),
  )
}

/**
 * While using Java 11 need to configure it this way or an error will be thrown:
 *   Execution failed for task ':decomat-examples:kspKotlin'.
 *   'compileJava' task (current target is 1.8) and 'kspKotlin' task (current target is 11) jvm target compatibility should be set to the same Java version.
 * See info on the issue here: https://github.com/google/ksp/issues/1288
 * Also here: https://youtrack.jetbrains.com/issue/KT-55947
 * Also here: https://stackoverflow.com/questions/69079963/how-to-set-compilejava-task-11-and-compilekotlin-task-1-8-jvm-target-com/76005200#76005200
 */
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}

//ksp {
//  // Passing an argument to the symbol processor.
//  // Change value to "true" in order to apply the argument.
//  arg("ignoreGenericArgs", "false")
//}

dependencies {
  implementation(project(":decomat-core"))
  testImplementation("org.testng:testng:7.1.0")
  ksp(project(":decomat-ksp"))
  testImplementation(kotlin("test"))
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}