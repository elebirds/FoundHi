package net.mcpes.foundhipro.core.app.command

/**
 * author: MagicDroidX
 * Nukkit Project
 */
abstract class Command(val name: String, val dec: String) {
    abstract fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean

    override fun toString(): String {
        return this.name + " " + this.dec
    }
}