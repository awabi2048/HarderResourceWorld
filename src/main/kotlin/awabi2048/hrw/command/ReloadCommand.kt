package awabi2048.hrw.command

import awabi2048.hrw.Main.Companion.MESSAGE_PREFIX
import awabi2048.hrw.config.Config
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object ReloadCommand: CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (!p3.isNullOrEmpty()) {
            p0.sendMessage("$MESSAGE_PREFIX §c無効なコマンドです。")
            return true
        }

        Config.loadAll()
        p0.sendMessage("$MESSAGE_PREFIX §aconfig.ymlをリロードしました。")
        return true
    }
}
