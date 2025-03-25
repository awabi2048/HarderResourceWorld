package awabi2048.hrw.config

import awabi2048.hrw.Lib
import awabi2048.hrw.Main.Companion.instance
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object BlockPlacementLog {
    private val dataFile = File(instance.dataFolder, "block_placement_log.yml")

    init {
        if (!dataFile.exists()) {
            instance.saveResource("block_placement_log.yml", true)
        }
    }

    private val log = YamlConfiguration.loadConfiguration(dataFile)

    fun addAttempt(location: Location) {
        if (location.world !in Config.APPLIED_WORLDS) return
        val world = location.world.name
        val currentLog = log.getStringList(world)
        val newLog = currentLog.add(Lib.toStringCoordinate(location))

        log.set(world, newLog)
    }

    fun removeAttempt(location: Location) {
        if (location.world !in Config.APPLIED_WORLDS) return
        val world = location.world.name

        val currentLog = log.getStringList(world)
        if (Lib.toStringCoordinate(location) !in currentLog) return

        val newLog = currentLog.remove(Lib.toStringCoordinate(location))

        log.set(world, newLog)
    }

    fun isLogged(location: Location): Boolean {
        if (location.world !in Config.APPLIED_WORLDS) return false
        val world = location.world.name
        val currentLog = log.getStringList(world)
        val stringLocation = Lib.toStringCoordinate(location)
        return stringLocation in currentLog
    }
}
