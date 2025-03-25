package awabi2048.hrw

import awabi2048.hrw.config.Config
import org.bukkit.Location

class LocalDifficulty(private val location: Location) {

    init {
        if (location.world !in Config.APPLIED_WORLDS) throw IllegalArgumentException("そのワールドはLocalDifficultyを持ちません。")
    }

    fun difficultyValue(): Double {
        return location.length() * Config.DISTANCE_DIFFICULTY_MULTIPLIER
    }
}
