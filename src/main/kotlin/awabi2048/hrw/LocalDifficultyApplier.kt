package awabi2048.hrw

import awabi2048.hrw.config.Config
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt
import kotlin.random.Random

class LocalDifficultyApplier(localDifficulty: LocalDifficulty) {
    private val difficulty = localDifficulty.difficultyValue()

    fun enhanceMob(mob: LivingEntity) {

        println(difficulty)

        // ステータス
        val baseHealth = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        val baseStrength = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: 3.0
        val baseMovementSpeed = mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.value ?: 0.2

        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = (baseHealth * difficulty * Config.DIFFICULTY_MOB_MULTIPLIER).coerceIn(baseHealth, 100.0)
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = (baseStrength * difficulty).coerceIn(baseStrength, 12.0)
        mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = (baseMovementSpeed * difficulty).coerceIn(baseMovementSpeed, 0.3)

        mob.health = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value?: 20.0

        // 装備s
        if (Random.nextDouble(0.0, 1.0) <= (difficulty / 10.0)) {
            val material = when (difficulty) {
                in 0.0..2.0 -> "WOODEN"
                in 2.0..<4.0 -> "STONE"
                in 4.0..<10.0 -> "IRON"
                else -> "GOLDEN"
            }

            val tool = listOf(
                "SWORD",
                "AXE",
                "PICKAXE"
            )

            val weaponItem = Material.getMaterial("${material}_${tool}") ?: return
            mob.equipment?.setItemInMainHand(ItemStack(weaponItem))
        }

        if (Random.nextDouble(0.0, 1.0) <= (difficulty / 10.0)) {
            val material = when (difficulty) {
                in 0.0..4.0 -> "LEATHER"
                in 4.0..<10.0 -> "IRON"
                else -> "GOLDEN"
            }

            val helmetItem = Material.getMaterial("${material}_HELMET") ?: return
            val bootsItem = Material.getMaterial("${material}_BOOTS") ?: return
            mob.equipment?.setItem(EquipmentSlot.HEAD, ItemStack(helmetItem))
            mob.equipment?.setItem(EquipmentSlot.FEET, ItemStack(bootsItem))
        }
    }

    fun modifyDrop(item: ItemStack) {
        item.amount += (difficulty * Config.DIFFICULTY_LOOT_MULTIPLIER).roundToInt()
    }

    fun modifiedDroppedExp(exp: Int): Int {
        return (exp * (difficulty * Config.DIFFICULTY_LOOT_MULTIPLIER).roundToInt())
    }
}
