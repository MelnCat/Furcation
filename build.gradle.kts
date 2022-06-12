import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	`java-library`
	`maven-publish`
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.serialization") version "1.6.21"
	id("io.papermc.paperweight.userdev") version "1.3.6"
	id("xyz.jpenilla.run-paper") version "1.0.6"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "cf.melncat.furcation"
version = "0.0.1"
description = "Core plugin for some plugins"

repositories {
	mavenCentral()
	mavenLocal()
	maven("https://repo.purpurmc.org/snapshots")
	maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation(kotlin("reflect"))
	paperweightDevBundle("org.purpurmc.purpur", "1.18.2-R0.1-SNAPSHOT")
	implementation("cloud.commandframework", "cloud-paper", "1.6.2")
	implementation("cloud.commandframework", "cloud-kotlin-extensions", "1.6.2")
	implementation("org.reflections", "reflections", "0.10.2")
	implementation("org.tukaani", "xz", "1.9")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.1")
	compileOnly("com.comphenix.protocol", "ProtocolLib", "4.8.0")

}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
			targetCompatibility = "17"
		}
	}
	shadowJar {
		fun reloc(pkg: String) = relocate(pkg, "cf.melcant.furcation.shaded.$pkg")
		listOf(
			"kotlin",
			"org.reflections",
			"org.tukaani.xz",
			"cloud.commandframework",
			"io.leangen.geantyref",
			"kotlinx"
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
	jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "1.8"
}