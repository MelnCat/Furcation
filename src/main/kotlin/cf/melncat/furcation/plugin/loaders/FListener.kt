package cf.melncat.furcation.plugin.loaders

import org.bukkit.event.Listener

interface FListener : Listener {
	fun shouldRegister() = true
}

annotation class RegisterListener