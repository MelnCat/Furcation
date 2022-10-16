package dev.melncat.furcation.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

public typealias TC = TextColor
public typealias NTC = NamedTextColor
public typealias TD = TextDecoration

private val default = MiniMessage.miniMessage()

private fun createTemplate(key: String, value: Any, parsed: Boolean)
	= if (value is ComponentLike) Placeholder.component(key, value)
	else if (parsed) Placeholder.parsed(key, value.toString())
	else Placeholder.unparsed(key, value.toString())

public fun String.component(color: TC, vararg decorations: TD): Component
	= Component.text(this, color, *decorations)

public operator fun Component.plus(other: Component): Component = append(other)

public fun String.mm(instance: MiniMessage = default): Component = instance.deserialize(this)

public fun String.mm(vararg placeholders: Pair<String, Any>, parsed: Boolean = false, instance: MiniMessage = default): Component =
	instance.deserialize(this, TagResolver.resolver(placeholders.map { (key, value) ->
		createTemplate(key, value, parsed)
	}))

public fun String.mm(vararg placeholders: Any, parsed: Boolean = false, instance: MiniMessage = default): Component =
	instance.deserialize(this, TagResolver.resolver(placeholders.mapIndexed { i, it ->
		createTemplate(i.toString(), it, parsed)
	}))
