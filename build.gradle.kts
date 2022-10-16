import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	`java-library`
	`maven-publish`
	kotlin("jvm") version "1.7.20-Beta"
	kotlin("plugin.serialization") version "1.6.21"
	id("io.papermc.paperweight.userdev") version "1.3.8"
	id("xyz.jpenilla.run-paper") version "1.0.6"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.melncat.furcation"
version = "0.0.2"
description = "Core plugin for some plugins"

repositories {
	mavenCentral()
	mavenLocal()
	maven("https://repo.purpurmc.org/snapshots")
	maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation(kotlin("reflect"))
	paperweightDevBundle("org.purpurmc.purpur", "1.19.2-R0.1-SNAPSHOT")

	implementation("cloud.commandframework:cloud-paper:1.7.1")
	implementation("cloud.commandframework:cloud-kotlin-extensions:1.7.1")
	implementation("cloud.commandframework:cloud-kotlin-coroutines:1.7.1")

	implementation("org.reflections", "reflections", "0.10.2")
	implementation("org.tukaani", "xz", "1.9")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
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
		fun reloc(pkg: String) = relocate(pkg, "dev.melncat.furcation.shaded.$pkg")
		listOf(
			"org.reflections",
			"org.tukaani.xz",
			"cloud.commandframework",
			"io.leangen.geantyref",
			"kotlinx",
			"org.jetbrains",
			"javassist"
		).forEach(::reloc)

		exclude(
			"com/google/gson/**",
			"org/slf4j/**",
			"org/intellij/lang/annotations/**",
			"javax/annotation/**"
		)
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