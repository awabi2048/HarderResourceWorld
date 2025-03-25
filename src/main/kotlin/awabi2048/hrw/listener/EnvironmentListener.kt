package awabi2048.hrw.listener

import awabi2048.hrw.LocalDifficulty
import awabi2048.hrw.LocalDifficultyApplier
import awabi2048.hrw.Main.Companion.ongoingBloodMoonEvents
import awabi2048.hrw.config.BlockPlacementLog
import awabi2048.hrw.config.Config
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
import org.bukkit.entity.Slime
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntitySpawnEvent
import kotlin.math.roundToInt

object EnvironmentListener : Listener {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val location = event.block.location.toBlockLocation()
        val isPlaced = BlockPlacementLog.isLogged(location)
        BlockPlacementLog.removeAttempt(location)

        if (isPlaced) return

        val material = event.block.type
        val extraDrops = Config.oreExtraDrops[material] ?: return
        extraDrops.forEach {
            it.dropAttempt(location)
        }
    }

    @EventHandler
    fun onMobSlay(event: EntityDeathEvent) {
        if (event.entity.killer == null) return
        val location = event.entity.location

        if (location.world !in Config.APPLIED_WORLDS) return
        if (event.entityType !in Config.mobExtraDrops.keys) return

        // ExtraDrop
        val extraDrops = Config.mobExtraDrops[event.entityType] ?: return
        extraDrops.forEach {
            it.dropAttempt(location)
        }

        // loot
        val difficulty = LocalDifficulty(location)
        val difficultyApplier = LocalDifficultyApplier(difficulty)
        event.drops.forEach {
            difficultyApplier.modifyDrop(it)
        }

        event.drops.forEach { it.amount += (LocalDifficulty(location).difficultyValue() * Config.DIFFICULTY_LOOT_MULTIPLIER).roundToInt() }
        event.droppedExp = difficultyApplier.modifiedDroppedExp(event.droppedExp)

        if (ongoingBloodMoonEvents.any { it.world == location.world }) {
            event.drops.forEach { it.amount *= 2 }
            event.droppedExp *= 2
        }
    }

    @EventHandler
    fun onMobSpawn(event: EntitySpawnEvent) {
        if (event.location.world !in Config.APPLIED_WORLDS) return
        if (event.entity is Monster || event.entity is Slime) {
            val mob = event.entity as LivingEntity
            val world = event.location.world

            // 赤い月の強化分
            if (ongoingBloodMoonEvents.any { it.world == world }) ongoingBloodMoonEvents.find { it.world == world }
                ?.enhanceMob(mob)

            // 難易度による強化分
            val localDifficulty = LocalDifficulty(event.location)
            val applier = LocalDifficultyApplier(localDifficulty)
            applier.enhanceMob(mob)
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val location = event.block.location.toBlockLocation()
        BlockPlacementLog.addAttempt(location)
    }
}
