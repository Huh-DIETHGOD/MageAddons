pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.terraformersmc.com/releases/")
        mavenCentral()
        gradlePluginPortal()
    }
}

val modName: String by settings
rootProject.name = modName
