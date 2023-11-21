package net.mcpes.foundhipro.core.app.command

import net.mcpes.foundhipro.core.logger.Logger

/**
 * author: MagicDroidX
 * Nukkit Project
 */
class ConsoleCommandSender : CommandSender {
    override val name: String
        get() = "控制台"

    override fun sendMessage(message: String) {
        for (line in message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            Logger.instance.info(line)
        }
    }
}
