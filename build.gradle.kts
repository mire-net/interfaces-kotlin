import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import xyz.jpenilla.runpaper.task.RunServer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.run.paper) apply false

    // Kotlin plugin prefers to be applied to parent when it's used in multiple sub-modules.
    kotlin("jvm") version "1.9.22" apply false
    alias(libs.plugins.spotless)
}

val javaVersion: Int = 21

allprojects {
    group = "com.noxcrew.interfaces"
    version = "1.1.8-SNAPSHOT"

    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply<SpotlessPlugin>()

    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")

        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/IslandPractice/interfaces-kotlin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GH_USER")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GH_TOKEN")
            }
        }
    }

    configure<SpotlessExtension> {
        kotlin {
            ktlint("0.47.1")
        }
    }

    // Configure any existing RunServerTasks
    tasks.withType<RunServer> {
        minecraftVersion("1.20.6")
        jvmArgs("-Dio.papermc.paper.suppress.sout.nags=true")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
            freeCompilerArgs += listOf("-Xexplicit-api=strict")
        }
    }
}
