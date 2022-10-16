package cf.melncat.furcation.plugin.loaders

import org.bukkit.event.Listener

public interface FListener : Listener {
	public fun shouldRegister(): Boolean = true
}

public annotation class RegisterListener