package net.mcpes.foundhipro.core.app.command

interface CommandSender {
    val name: String

    fun sendMessage(message: String)
}
