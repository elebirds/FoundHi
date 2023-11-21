package net.mcpes.foundhi.core.plugin.gui.window.lambda.defaults

import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.function.Variable
import net.mcpes.foundhi.core.plugin.gui.window.lambda.LambdaModalGUI

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/14
 * version 1.0
 */
class LambdaVariableModalGUI(id: String, title: String, content: String) : LambdaModalGUI(id, title, content), Variable {
    override fun open(player: NukkitPlayer, params: Array<out Any>): Int {
        var f = this.content
        params.forEachIndexed { index, s -> f = f.replace("%$index", s.toString()) }
        return player.showGUI(this.data.replace(this.content, f), this)
    }
}