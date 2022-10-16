@file:Suppress("MemberVisibilityCanBePrivate")

package cf.melncat.furcation.plugin

import cf.melncat.furcation.plugin.loaders.FCommand
import cf.melncat.furcation.plugin.loaders.FListener
import cf.melncat.furcation.plugin.loaders.RegisterCommand
import cf.melncat.furcation.plugin.loaders.RegisterListener
import cf.melncat.furcation.util.NTC
import cf.melncat.furcation.util.component
import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.exceptions.InvalidCommandSenderException
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.vehicle.Boat

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.scanners.Scanners.*
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import java.lang.Compiler.disable
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
	protected lateinit var commandManager: PaperCommandManager<CommandSender>

	private fun loadCommandManager() {
		commandManager = PaperCommandManager(
			this,
			CommandExecutionCoordinator.simpleCoordinator(),
			{ it }, { it }
		)
		//if (commandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER))
		//commandManager.registerBrigadier()
		if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION))
			commandManager.registerAsynchronousCompletions()
		commandManager.registerExceptionHandler(InvalidCommandSenderException::class.java) { s, e ->
			s.sendMessage("You must be a ${e.requiredSender.simpleName} to use this command.".component(NTC.RED))
		}
	}

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

	private fun loadCommands() {
		val registered = reflections.get(
			SubTypes.of<Any>(
				TypesAnnotated.with(
					RegisterCommand::class.java
				)
			).asClass<Any>()
		)
		for (command in registered) {
			val obj = command.kotlin.objectInstance ?: try {
				command.getConstructor().newInstance()
			} catch (e: NoSuchMethodException) {
				try {
					command.getConstructor(FPlugin::class.java).newInstance(this)
				} catch (e: NoSuchMethodException) {
					throw IllegalStateException("Command ${command.name} does not have an object instance" +
							" or a constructor with either no arguments or a single plugin argument.")
				}
			}
			if (obj !is FCommand) {
				logger.warning("Attempted to use RegisterCommand to register non-command ${command.name}.")
				continue
			}
			obj.register(commandManager)
		}
	}

	final override fun onEnable() {
		loadCommandManager()
		enable()
		loadListeners()
		loadCommands()
	}

	public open fun enable() {}

	final override fun onDisable() {
		disable()
	}

	public open fun disable() {}

	final override fun onLoad() {
		load()
	}

	public open fun load() {}
}