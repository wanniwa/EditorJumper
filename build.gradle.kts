plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "github.wanniwa"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Gson is bundled inside IntelliJ Platform; compileOnly avoids packaging a duplicate jar.
    compileOnly("com.google.code.gson:gson:2.10.1")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2.6")
    type.set("IC") // Target IDE Platform
    updateSinceUntilBuild.set(false)
    sameSinceUntilBuild.set(false)
    plugins.set(listOf(/* Plugin Dependencies */))

}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    // Configure processResources to handle UTF-8 encoding for properties files
    processResources {
        filteringCharset = "UTF-8"
    }

    patchPluginXml {
        sinceBuild.set("231")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
