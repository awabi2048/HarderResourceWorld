package awabi2048.hrw

import awabi2048.hrw.command.ReloadCommand
import awabi2048.hrw.config.Config
import awabi2048.hrw.listener.BloodMoonListener
import awabi2048.hrw.listener.EnvironmentListener
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin
        const val MESSAGE_PREFIX = "§7«§4HRW§7»"
        val ongoingBloodMoonEvents: MutableSet<BloodMoonEvent> = mutableSetOf()
    }

    override fun onEnable() {
        instance = this

        // config 読み込み
        Config.loadAll()

        // EventListener
        server.pluginManager.registerEvents(EnvironmentListener, instance)
        server.pluginManager.registerEvents(BloodMoonListener, instance)

        // command
        getCommand("hrwreload")?.setExecutor(ReloadCommand)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
