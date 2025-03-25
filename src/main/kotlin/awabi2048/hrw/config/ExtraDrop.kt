package awabi2048.hrw.config

import org.bukkit.Location
import kotlin.random.Random

class ExtraDrop(
    val item: UniqueItem,
    val probability: Double,
) {
    init {
        if (probability < 0.0 || probability > 1.0) {
            throw IllegalArgumentException("probabilityが無効です。")
        }

        if (item !in Config.uniqueItems.values) {
            throw IllegalArgumentException("uniqueItem が未定義です。")
        }
    }

    fun dropAttempt(location: Location) {
        val modifiedProbability = (probability * (location.length() * Config.DISTANCE_DIFFICULTY_MULTIPLIER))
            .coerceIn(0.0, 0.2)

        if (Random.nextDouble(0.0, 1.0) <= modifiedProbability) {
            val itemStack = item.itemStack()
            location.world.dropItemNaturally(location, itemStack)
        }
    }
}
