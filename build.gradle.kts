buildscript {
    repositories {
        mavenCentral()
    }
}

repositories {
    mavenCentral()
}

plugins {
    id("java-library")
//    id("org.graalvm.buildtools.native") version "0.9.17"
}

dependencies {
    implementation("info.picocli:picocli:4.6.3")
    annotationProcessor("info.picocli:picocli-codegen:4.6.3")

    implementation("org.postgresql:postgresql:42.5.0")
    implementation("com.h2database:h2:2.1.214")
    implementation("mysql:mysql-connector-java:8.0.31")

    compileOnly("org.graalvm.nativeimage:svm:22.2.0")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

}

//graalvmNative {
//    agent {
//        defaultMode.set("standard")
//    }
//    binaries {
//        named("main") {
//            imageName.set("sqlctl")
//        }
//    }
//    metadataRepository {
//        enabled.set(true)
//    }
//}

tasks.jar {
    archiveFileName.set("${project.name}.jar")
    manifest.attributes["Main-Class"] = "com.dexecr.Main"
    manifest.attributes["Class-Path"] = configurations
        .runtimeClasspath
        .get()
        .joinToString(separator = " ") { it.name }
}

task("copyDependencies", Copy::class) {
    configurations.compileClasspath.get()
        .filter { it.extension == "jar" }
        .forEach { from(it.absolutePath).into("$buildDir/libs") }
}

tasks["build"].dependsOn("copyDependencies")


group = "com.dexecr"
version = "0.0.1-SNAPSHOT"
