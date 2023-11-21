package net.mcpes.foundhi.core.plugin.gui.window.event

import cn.nukkit.form.window.FormWindowModal
import net.mcpes.foundhi.core.plugin.gui.window.EventGUI
import net.mcpes.foundhi.core.plugin.gui.window.ModalGUI

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
open class EventModalGUI(id: String, title: String, content: String) : ModalGUI(ProcessMode.EVENT, id, title, content), EventGUI {
    override fun analysis(data: String): HashMap<String, Any> {
        val map: HashMap<String, Any> = HashMap()
        if (gui !is FormWindowModal) return map
        return if (data == "true") {
            map["text"] = this.gui.button1
            map["id"] = 0
            map
        } else {
            map["text"] = this.gui.button2
            map["id"] = 1
            map
        }
    }
}