package cf.melncat.furcation.plugin.loaders

import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.command.CommandSender

public interface FCommand {
	public fun register(manager: PaperCommandManager<CommandSender>)
}

public annotation class RegisterCommand