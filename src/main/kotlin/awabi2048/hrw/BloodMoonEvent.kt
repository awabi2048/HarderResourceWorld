package awabi2048.hrw

import awabi2048.hrw.Main.Companion.instance
import awabi2048.hrw.Main.Companion.ongoingBloodMoonEvents
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity

class BloodMoonEvent(val world: World) {
    fun initiate() {
        // 登録
        ongoingBloodMoonEvents.add(this)

        // プレイヤー演出
        world.players.forEach {
            it.sendMessage("§4恐ろしい赤い月の夜が来たようです...。")
            it.playSound(it, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 0.5f)
            it.playSound(it, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.75f)
        }

        // 天気演出
        world.isThundering = true
        world.setStorm(true)

        // 終了時スケジュール: その前にシャットダウンした場合は登録が忘れられるのでOK
        Bukkit.getScheduler().runTaskLater(
            instance,
            Runnable {
                finish()
            },
            6000L // 夜が終わるまで
        )
    }

    fun enhanceMob(mob: LivingEntity) {
        // ステータス
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue?.times(1.5)
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue?.times(1.3)
    }

    private fun finish() {
        world.isThundering = false
        ongoingBloodMoonEvents.remove(this)
    }
}
