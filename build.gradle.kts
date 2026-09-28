plugins {
    kotlin("jvm") version "2.0.20"
    id("fabric-loom") version "1.9-SNAPSHOT"
    idea
    java
}

val modName: String by project
val modID: String by project
val modVersion: String by project
val minecraftVersion: String by project
val yarnMappings: String by project
val loaderVersion: String by project
val fabricVersion: String by project
val fabricKotlinVersion: String by project
val clothConfigVersion: String by project

version = modVersion
group = "mageaddons"

base {
    archivesName.set(modName)
}

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.fabricmc.net/")
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")

    // Fabric Language Kotlin
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    // Cloth Config for configuration GUI
    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    // ModMenu integration
    modImplementation("com.terraformersmc:modmenu:10.0.0")
}

loom {
    accessWidenerPath.set(file("src/main/resources/mageaddons.accesswidener"))

    runs {
        getByName("client") {
            ideConfigGenerated(true)
        }
    }
}

tasks {
    processResources {
        inputs.property("modname", modName)
        inputs.property("modid", modID)
        inputs.property("version", project.version)
        inputs.property("minecraftVersion", minecraftVersion)

        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "modname" to modName,
                    "modid" to modID,
                    "version" to project.version,
                    "minecraftVersion" to minecraftVersion,
                ),
            )
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
}

kotlin {
    jvmToolchain(21)
}
