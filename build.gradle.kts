plugins {
    kotlin("jvm") version "2.2.20"
    application
}

group = "kr.najoan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    // Discord JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.24")

    // Google Generative AI (Gemini API)
    implementation("com.google.genai:google-genai:1.25.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("kr.najoan.discord.BotMainKt")
}