package cf.melncat.furcation.inventorygui

import cf.melncat.furcation.util.mm
import net.kyori.adventure.text.Component
import net.minecraft.world.inventory.Slot
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

public class InventoryGUI(
	public val rows: Int, title: Component
	) {
	public var title: Component = title
		set(value) {
			val newInv = Bukkit.createInventory(null, inventory.size, value)
			inventory.contents?.forEachIndexed { i, x ->
				if (x != null)  newInv.setItem(i, x)
			}
			inventory = newInv
			field = value
		}
	public constructor (rows: Int, title: String) : this(rows, title.mm())

	public var inventory: Inventory = Bukkit.createInventory(null, rows * 9, title)
	private set

			private
	val items: MutableMap<Int, Slot> = mutableMapOf()

	public fun handleClick(event: InventoryClickEvent) {

	}

	public inner class Slot(
		public val gui: InventoryGUI,
		public val slot: Int,
		item: ItemStack,
		public val callback: Slot.(event: InventoryClickEvent) -> Unit
	) {
		public var item: ItemStack?
			get() = gui.inventory.getItem(slot)
			set(item) = gui.inventory.setItem(slot, item)

	}
}
