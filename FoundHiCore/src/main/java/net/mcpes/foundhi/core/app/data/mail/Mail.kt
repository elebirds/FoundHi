package net.mcpes.foundhi.core.app.data.mail

import net.mcpes.foundhi.core.dao.mail.MailDatabase
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/19
 * version 1.0
 */
class Mail : MailInterfate {
    val sender: String
    val id: String
    val type: Int
    val addressee: String
    val content: String
    val attachment: HashMap<String, Any>
    var read: Boolean
    val loadFun: Boolean
    val db: String

    constructor(id: String, type: Int, db: String, addressee: String, sender: String, content: String, attachment: HashMap<String, Any>, read: Boolean) {
        this.sender = sender
        this.db = db
        this.id = id
        this.type = type
        this.addressee = addressee
        this.content = content
        this.attachment = attachment
        this.read = read
        this.loadFun = true
    }

    constructor(date: Int, id: String) {
        this.id = id
        this.db = "mail_$date"
        this.sender = MailDatabase.getSender(id, this.db)
        this.type = MailDatabase.getType(id, this.db)
        this.addressee = MailDatabase.getAddressee(id, this.db)
        this.content = MailDatabase.getContent(id, this.db)
        this.attachment = MailDatabase.getAttachmentMap(id, this.db)
        this.read = MailDatabase.getReadStatus(id, this.db) == 1
        this.loadFun = false
    }

    fun save() {
        if (this.loadFun) {
            MailDatabase.addMail(this)
        } else {
            MailDatabase.setReadStatus(id, db, this.read)
        }
    }

    override fun toString(): String {
        return id
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json["s"] = this.sender
        json["i"] = this.id
        json["t"] = this.type
        json["d"] = this.addressee
        json["c"] = this.content
        json["a"] = JSONObject.fromObject(this.attachment).toString()
        json["r"] = this.read
        json["b"] = this.db
        return json
    }
}