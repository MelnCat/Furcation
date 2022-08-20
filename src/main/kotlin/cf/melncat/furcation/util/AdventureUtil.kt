package cf.melncat.furcation.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

typealias TC = TextColor
typealias NTC = NamedTextColor
typealias TD = TextDecoration

private val default = MiniMessage.miniMessage()

fun String.component(color: TC, vararg decorations: TD)
	= Component.text(this, color, *decorations)

operator fun Component.plus(other: Component) = append(other)

fun String.mm(instance: MiniMessage = default) = instance.deserialize(this)

fun String.mm(vararg placeholders: Pair<String, String>, parsed: Boolean = false, instance: MiniMessage = default) =
	instance.deserialize(this, TagResolver.resolver(placeholders.map {
		if (parsed) Placeholder.parsed(it.first, it.second)
		else Placeholder.unparsed(it.first, it.second)
	}))

fun String.mm(vararg placeholders: String, parsed: Boolean = false, instance: MiniMessage = default) =
	instance.deserialize(this, TagResolver.resolver(placeholders.mapIndexed { i, it ->
		if (parsed) Placeholder.parsed(i.toString(), it)
		else Placeholder.unparsed(i.toString(), it)
	}))

object A {}