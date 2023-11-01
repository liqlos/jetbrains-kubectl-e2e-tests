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
   testImplementation(kotlin("stdlib"))
   testImplementation("io.kotest:kotest-runner-junit5:5.3.2")
   runtimeOnly("io.github.microutils:kotlin-logging-jvm:3.0.5")
   testImplementation("ch.qos.logback:logback-classic:'1.4.6'")
}

tasks.withType<Test> {
   useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
   kotlinOptions {
      jvmTarget = "17"
   }
}
