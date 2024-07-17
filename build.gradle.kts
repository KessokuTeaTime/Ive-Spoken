plugins {
	base
	java
	idea
	`maven-publish`
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.modpublisher)
}

val display = libs.versions.display

group = libs.versions.maven.group.get()
version = "${libs.versions.mod.get()}-${libs.versions.loader.get()}.${libs.versions.minecraft.get()}"

base {
	archivesName.set(libs.versions.archives.name)
}

repositories {
	mavenCentral()
	maven { url = uri("https://jitpack.io") }
	maven { url = uri("https://maven.shedaniel.me/") } // Cloth Config
	maven { url = uri("https://maven.terraformersmc.com/releases/") } // Mod Menu
}

dependencies {
	minecraft(libs.minecraft)
	mappings(libs.yarn)
	modImplementation(libs.bundles.fabric)

	modApi(libs.cloth.config)
	modApi(libs.modmenu)
	modApi(libs.night.auto.config)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17

	withSourcesJar()
}

tasks {
	processResources {
		filesMatching("fabric.mod.json") {
			expand(mapOf(
					"version" to libs.versions.mod.get(),
					"display" to display
			))
		}
	}

	jar {
		from("LICENSE")
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	repositories {
	}
}

publisher {
	apiKeys {
		modrinth(System.getenv("MODRINTH_TOKEN"))
		curseforge(System.getenv("CURSEFORGE_TOKEN"))
	}

	modrinthID.set(libs.versions.id.modrinth)
	curseID.set(libs.versions.id.curseforge)

	versionType.set("release")
	projectVersion.set(project.version.toString())
	gameVersions.set(listOf("1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6"))
	loaders.set(listOf("fabric", "quilt"))
	curseEnvironment.set("both")

	modrinthDepends.required("fabric-api", "night-auto-config")
	modrinthDepends.optional()
	modrinthDepends.embedded()

	curseDepends.required("fabric-api", "night-auto-config")
	curseDepends.optional()
	curseDepends.embedded()

	displayName.set("${display.name.get()} ${libs.versions.mod.get()} for ${display.loader.get()} ${display.version.get()}")

	artifact.set(tasks.remapJar)
	addAdditionalFile(tasks.remapSourcesJar)

	changelog.set(file("CHANGELOG.md"))
}
