package cf.melncat.furcation.internal

import cf.melncat.furcation.util.mm
import cloud.commandframework.ArgumentDescription
import cloud.commandframework.bukkit.CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin

public class Furcation : JavaPlugin() {
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
		if (commandManager.hasCapability(ASYNCHRONOUS_COMPLETION)) commandManager.registerAsynchronousCompletions()

		commandManager.buildAndRegister(
			"furcation",
			ArgumentDescription.of("Information about the Furcation plugin."),
			arrayOf("fc")
		) {
			handler { ctx ->
				ctx.sender.sendMessage("<yellow>TODO <green>THIS</green> foo".mm())
			}
		}
		logger.info("Furcation successfully loaded.")

	}
}