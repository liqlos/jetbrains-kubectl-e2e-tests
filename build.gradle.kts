import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
   kotlin("jvm") version "1.9.20"
}

repositories {
   mavenCentral()
}

group = "com.bukhalov"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
   implementation(kotlin("stdlib"))
   implementation("io.kotest:kotest-runner-junit5:5.3.2")
   implementation("ch.qos.logback:logback-classic:1.4.6")
   implementation("org.slf4j:slf4j-api:2.0.9")
   implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
   implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
}

tasks.withType<Test> {
   useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
   kotlinOptions {
      jvmTarget = "17"
   }
}
