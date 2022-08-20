@file:Suppress("MemberVisibilityCanBePrivate")

package cf.melncat.furcation.plugin

import cf.melncat.furcation.plugin.loaders.FListener
import cf.melncat.furcation.plugin.loaders.RegisterListener
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.vehicle.Boat

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.scanners.Scanners.*
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import kotlin.reflect.full.isSubclassOf

public abstract class FPlugin(public val rootPackage: String) : JavaPlugin() {
	protected val reflections: Reflections = Reflections(
		ConfigurationBuilder()
			.setClassLoaders(arrayOf(javaClass.classLoader))
			.forPackage(rootPackage)
			.filterInputsBy(FilterBuilder().includePackage(rootPackage))
			.setScanners(*Scanners.values())
			.setUrls(file.toURI().toURL())
	)

	private fun loadListeners() {
		val registered = reflections.get(
			SubTypes.of<Any>(
				TypesAnnotated.with(
					RegisterListener::class.java
				)
			).asClass<Any>()
		)
		for (listener in registered) {
			val obj = listener.kotlin.objectInstance ?: try {
				listener.getConstructor().newInstance()
			} catch (e: NoSuchMethodException) {
				try {
					listener.getConstructor(FPlugin::class.java).newInstance(this)
				} catch (e: NoSuchMethodException) {
					throw IllegalStateException("Listener ${listener.name} does not have an object instance" +
							" or a constructor with either no arguments or a single plugin argument.")
				}
			}
			if (obj is FListener) {
				if (!obj.shouldRegister()) {
					logger.info("Listener ${listener.name} will not be registered.")
					continue
				}
			}
			if (obj !is Listener) {
				logger.warning("Attempted to use RegisterListener to register non-listener ${listener.name}.")
				continue
			}
			server.pluginManager.registerEvents(obj, this)
		}
	}

	final override fun onEnable() {
		enable()
		loadListeners()
	}

	open fun enable() {}

	final override fun onDisable() {
		disable()
	}

	open fun disable() {}

	final override fun onLoad() {
		load()
	}

	open fun load() {}
}