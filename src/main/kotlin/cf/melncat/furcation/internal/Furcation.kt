package cf.melncat.furcation.internal

import cf.melncat.furcation.util.A
import cf.melncat.furcation.util.mm
import cf.melncat.furcation.util.server
import cloud.commandframework.ArgumentDescription
import cloud.commandframework.bukkit.CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION
import cloud.commandframework.bukkit.CloudBukkitCapabilities.BRIGADIER
import cloud.commandframework.execution.CommandExecutionCoordinator
import net.minecraft.world.food.Foods
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import cloud.commandframework.kotlin.MutableCommandBuilder
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault.TRUE
import kotlin.reflect.full.memberProperties

class Furcation : JavaPlugin() {
	private val permissions = listOf<Permission>(
	//	Permission("furcation.command.furcation", TRUE)
	)
	private lateinit var commandManager: PaperCommandManager<CommandSender>

	override fun onEnable() {
		try {

		} catch (e: ClassNotFoundException) {
			throw IllegalStateException(
				"Furcation requires the server to be running Paper or a fork. " +
						"The server is currently running ${server.name}."
			)
		}
		commandManager = PaperCommandManager(
			this,
			CommandExecutionCoordinator.simpleCoordinator(),
			{ it }, { it }
		)
		permissions.forEach {
			Bukkit.getPluginManager().permissions.add(it)
		}
		if (commandManager.queryCapability(ASYNCHRONOUS_COMPLETION)) commandManager.registerAsynchronousCompletions()

		commandManager.buildAndRegister(
			"furcation",
			ArgumentDescription.of("Information about the Furcation plugin."),
			arrayOf("fc")
		) {
			handler { ctx ->
				ctx.sender.sendMessage("<yellow>TODO <green>THIS</green> foo".mm())
			}
		}
		println(Any::class)
		println(Any::class.memberProperties)
		println(Any::class.java.classLoader)
		println(A::class.objectInstance)
		logger.info("Furcation successfully loaded.")
	}
}