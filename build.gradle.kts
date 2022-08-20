import io.papermc.paperweight.tasks.generateMappings
import io.papermc.paperweight.util.constants.PARAM_MAPPINGS_CONFIG
import io.papermc.paperweight.util.convertToPath
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	`java-library`
	`maven-publish`
	kotlin("jvm") version "1.7.20-Beta"
	kotlin("plugin.serialization") version "1.6.21"
	id("io.papermc.paperweight.userdev") version "1.3.7"
	id("xyz.jpenilla.run-paper") version "1.0.6"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "cf.melncat.furcation"
version = "0.0.1"
description = "Core plugin for some plugins"

repositories {
	mavenCentral()
	mavenLocal()
	maven("https://repo.papermc.io/repository/maven-public/")
	maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation(kotlin("reflect"))
	paperDevBundle("1.19.2-R0.1-SNAPSHOT")
	compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
	implementation("cloud.commandframework:cloud-paper:1.7.0")
	implementation("cloud.commandframework:cloud-kotlin-extensions:1.7.0")
	implementation("org.reflections", "reflections", "0.10.2")
	implementation("org.tukaani", "xz", "1.9")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
	compileOnly("com.comphenix.protocol", "ProtocolLib", "4.8.0")

}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
	explicitApiWarning()
}


tasks {
	assemble {
		dependsOn(reobfJar)
	}
	compileJava {
		options.encoding = Charsets.UTF_8.name()
		options.release.set(17)
	}
	javadoc {
		options.encoding = Charsets.UTF_8.name()
	}
	processResources {
		filteringCharset = Charsets.UTF_8.name()
	}
	compileKotlin {
		kotlinOptions {
			jvmTarget = "17"
		}
	}
	shadowJar {
		fun reloc(pkg: String) = relocate(pkg, "cf.melncat.furcation.shaded.$pkg")
		listOf(
			"kotlin",
			"org.reflections",
			"org.tukaani.xz",
			"cloud.commandframework",
			"io.leangen.geantyref",
			"kotlinx",
			"org.jetbrains"
		).forEach(::reloc)
	}
}
val ver = version.toString()
publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = group.toString()
			artifactId = "Furcation"
			version = ver

			from(components["java"])
		}
	}
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "17"
}