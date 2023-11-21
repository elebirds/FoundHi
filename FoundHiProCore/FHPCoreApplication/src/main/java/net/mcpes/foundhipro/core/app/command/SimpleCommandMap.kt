package net.mcpes.foundhipro.core.app.command

import java.util.*

/**
 * author: MagicDroidX
 * Nukkit Project
 */
open class SimpleCommandMap {
    private val knownCommands: MutableMap<String, Command> = HashMap()
    var executeCommands = ArrayList<Array<Any>>()

    val commands: Map<String, Command>
        get() = knownCommands

    fun register(command: Command): Boolean {
        return this.register(command, null)
    }

    fun register(command: Command, label: String?): Boolean {
        var n = label
        if (n == null) {
            n = command.name
        }
        n = n.trim { it <= ' ' }.toLowerCase()
        if (knownCommands.containsKey(n)) return false
        knownCommands[n] = command
        return true
    }

    private fun parseArguments(cmdLine: String): ArrayList<String> {
        val sb = StringBuilder(cmdLine)
        val args = ArrayList<String>()
        var notQuoted = true
        var start = 0

        var i = 0
        while (i < sb.length) {
            if (sb[i] == '\\') {
                sb.deleteCharAt(i)
                i++
                continue
            }

            if (sb[i] == ' ' && notQuoted) {
                val arg = sb.substring(start, i)
                if (!arg.isEmpty()) {
                    args.add(arg)
                }
                start = i + 1
            } else if (sb[i] == '"') {
                sb.deleteCharAt(i)
                --i
                notQuoted = !notQuoted
            }
            i++
        }

        val arg = sb.substring(start)
        if (!arg.isEmpty()) {
            args.add(arg)
        }
        return args
    }

    fun dispatch(sender: CommandSender, cmdLine: String): Boolean {
        val parsed = parseArguments(cmdLine)
        if (parsed.size == 0) {
            return false
        }
        val sentCommandLabel = parsed.removeAt(0).toLowerCase()
        val args = parsed.toTypedArray()
        val target = this.getCommand(sentCommandLabel) ?: return false
        target.execute(sender, sentCommandLabel, args)
        return true
    }

    fun getCommand(name: String): Command? {
        return if (this.knownCommands.containsKey(name)) {
            this.knownCommands[name]
        } else null
    }
}