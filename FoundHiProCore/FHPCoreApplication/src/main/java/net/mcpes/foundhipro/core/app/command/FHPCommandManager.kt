package net.mcpes.foundhipro.core.app.command

import net.mcpes.foundhipro.core.app.CoreServer
import net.mcpes.foundhipro.core.app.command.defaults.HelpCommand
import net.mcpes.foundhipro.core.app.command.defaults.StopCommand

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/25
 * version 1.0
 */
object FHPCommandManager {
    lateinit var commandMap: SimpleCommandMap
    lateinit var commandReader: CommandReader

    fun boot() {
        this.commandMap = SimpleCommandMap()
        this.commandReader = CommandReader()
        CoreServer.instance.scheduler.scheduleAsyncTask(this.commandReader)
        this.setDefaultCommands()
    }

    private fun setDefaultCommands() {
        this.commandMap.register(StopCommand())
        this.commandMap.register(HelpCommand())
    }

    /**
     * 执行API端命令
     *
     * @param commandLine String 命令<br></br>
     * @param sender      CommandSender 命令执行者
     */
    fun dispatchCommand(sender: CommandSender?, commandLine: String): Boolean {
        if (sender == null) {
            return false
        }
        if (this.commandMap.dispatch(sender, commandLine)) {
            return true
        }
        sender.sendMessage("§c未知的指令。请使用 /help 来显示指令列表。")
        return false
    }
}