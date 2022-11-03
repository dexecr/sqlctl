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
    id("org.graalvm.buildtools.native") version "0.9.14"
}

dependencies {
    implementation("info.picocli:picocli:4.6.3")
    annotationProcessor("info.picocli:picocli-codegen:4.6.3")

    implementation("org.postgresql:postgresql:42.5.0")
    implementation("com.h2database:h2:2.1.214")
    implementation("mysql:mysql-connector-java:8.0.31")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

}

tasks.jar {
    archiveFileName.set("${project.name}.jar")
    manifest.attributes["Main-Class"] = "com.dexecr.Main"
    manifest.attributes["Class-Path"] = configurations
        .runtimeClasspath
        .get()
        .joinToString(separator = " ") { file ->
            "libs/${file.name}"
        }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


group = "com.dexecr"
version = "0.0.1-SNAPSHOT"
