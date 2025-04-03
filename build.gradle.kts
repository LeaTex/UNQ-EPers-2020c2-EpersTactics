import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.72"
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"

}

group = "ar.edu.unq.epers.tactics"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")


    implementation("com.h2database:h2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation ("junit:junit:4.12")

    runtimeOnly("mysql:mysql-connector-java")
}
dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testCompile (group= "junit", name= "junit", version= "4.12")
    testCompile (group= "io.kotlintest", name= "kotlintest-runner-junit4", version= "3.4.2")

    testCompile (group= "org.slf4j", name= "slf4j-log4j12", version= "1.7.30")
    compile (group= "org.apache.logging.log4j", name= "log4j-core", version= "2.13.1")

    compile ("org.neo4j.driver:neo4j-java-driver:4.0.1")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testCompile (group= "junit", name= "junit", version= "4.12")
    testCompile  (group= "io.kotlintest", name= "kotlintest-runner-junit4", version= "3.4.2")

    compile ("org.mongodb:mongodb-driver-sync:4.1.1")
    compile ("org.mongodb:mongodb-driver-core:4.1.1")
    compile (group= "org.mongodb", name= "bson", version= "4.1.1")
    testCompile  ( group="org.slf4j", name= "slf4j-log4j12", version= "1.7.30")
    compile (group= "org.apache.logging.log4j", name= "log4j-core", version= "2.13.1")
}
dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
}
tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
