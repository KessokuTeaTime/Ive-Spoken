pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/")
		maven("https://maven.architectury.dev/")
		maven("https://maven.neoforged.net/releases/")
		maven("https://maven.firstdark.dev/releases")
		gradlePluginPortal()
	}
}

rootProject.name = "Ive-Spoken"

include("common")
include("fabric")
include("neoforge")
