package awabi2048.hrw.listener

import awabi2048.hrw.BloodMoonEvent
import awabi2048.hrw.Main.Companion.instance
import awabi2048.hrw.config.Config
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

object BloodMoonListener : Listener {
    @EventHandler
    fun onPlayerJoinWorld(event: PlayerChangedWorldEvent) {
        if (event.player.world !in Config.APPLIED_WORLDS) return
        if (event.player.world.players.size != 1) return // 無人のワールドに移動したときのみスケジュール処理

        val world = event.player.world
        val currentWorldTime = world.time
        val scheduleTime = if (currentWorldTime in 13000L..23000) 13000L + 24000L - currentWorldTime else 13000L - currentWorldTime // すでに夜なら翌晩にスケジュールする

        object : BukkitRunnable() {
            override fun run() {
                if (world.players.isEmpty()) cancel()
                if (Random.nextDouble(0.0, 1.0) <= Config.BLOOD_MOON_RATE) {
                    val bloodMoonEvent = BloodMoonEvent(world)
                    bloodMoonEvent.initiate()
                }
            }
        }.runTaskTimer(instance, scheduleTime, 24000L)
    }
}
