package net.mcpes.foundhipro.core.app.command

import net.mcpes.foundhipro.core.app.scheduler.AsyncTask
import java.io.IOException

/**
 * author: MagicDroidX
 * Nukkit
 */
class CommandReader : AsyncTask() {
    private var running = true
    private var consoleSender: ConsoleCommandSender = ConsoleCommandSender()

    fun shutdown() {
        this.running = false
    }

    override fun onRun() {
        try {
            while (this.running) {
                Thread.sleep(50)
                var line = readLine()
                while (line != null) {
                    if (!line.trim { it <= ' ' }.isEmpty()) {
                        FHPCommandManager.dispatchCommand(this.consoleSender, line)
                        line = readLine()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        var instance: CommandReader? = null
    }
}
