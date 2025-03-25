package awabi2048.hrw.config

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class UniqueItem(
    private val material: Material,
    private val itemName: String?,
    private val lore: List<String>?,
    private val customModelData: Int?
) {
    fun itemStack(): ItemStack {
        val item = ItemStack(material)
        item.editMeta {
            if (itemName != null) it.itemName(Component.text(itemName))
            if (lore != null) it.lore(lore.map {element -> Component.text(element)})
            if (customModelData != null) it.setCustomModelData(customModelData)
        }

        return item
    }
}
