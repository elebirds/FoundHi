package net.mcpes.foundhi.core.plugin.data

import net.mcpes.foundhi.core.app.data.mail.MailInterfate

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/20
 * version 1.0
 */
data class MailCache(val id: String, val type: Int, val db: String, val addressee: String, val sender: String, val content: String, val attachment: HashMap<String, Any>, var read: Boolean) : MailInterfate