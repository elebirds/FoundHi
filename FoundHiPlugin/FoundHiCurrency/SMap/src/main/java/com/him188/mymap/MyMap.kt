package com.him188.mymap

import cn.nukkit.Server
import cn.nukkit.permission.Permission
import cn.nukkit.utils.Config
import com.him188.mymap.event.FrameAddEvent
import com.him188.mymap.event.FrameRemoveEvent
import com.him188.mymap.utils.FrameList
import com.him188.mymap.utils.LanguageBase
import com.him188.mymap.utils.Utils
import net.mcpes.foundhi.core.plugin.modal.FoundHiPluginBase
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author Him188 @ MyMap Project
 */
class MyMap : FoundHiPluginBase() {
    private var commonListener: CommonListener? = null
    private var settingListener: SettingListener? = null
    private var protectionListener: FrameProtectionListener? = null
    lateinit var list: FrameList
        private set

    init {
        instance = this
    }

    override fun onEnable() {
        super.onEnable()

        MyMapFrame.IMAGE_DATA_FOLDER = File(dataFolder, "images")
        MyMapFrame.DEFAULT_IMAGE_FILE = Utils.detectImageFile(File(MyMapFrame.IMAGE_DATA_FOLDER, "default").path)
        MyMapFrame.IMAGE_DATA_FOLDER.mkdir()
        MyMapFrame.FRAME_DATA_FOLDER = File(dataFolder, "frames")
        MyMapFrame.FRAME_DATA_FOLDER.mkdir()
        saveResource("default.jpg", "images/default.jpg", false)

        saveDefaultConfig()
        reloadConfig()
        LanguageBase.CURRENT_LANGUAGE = LanguageBase.CHINESE
        list = FrameList()

        val files = MyMapFrame.FRAME_DATA_FOLDER.listFiles()
        if (files != null) {
            for (file in files) {
                try {
                    list.add(MyMapFrame.fromConfigSection(Config(file, Config.YAML).rootSection))
                } catch (e: IOException) {
                    logger.error(LanguageBase.getMessage(LanguageBase.ID.LOAD_FRAME_ERROR, file.name), e)
                }

            }
        }

        if (settingListener == null) {
            settingListener = SettingListener(this)
            server.pluginManager.registerEvents(settingListener!!, this)
        }

        if (commonListener == null) {
            commonListener = CommonListener(this)
            server.pluginManager.registerEvents(commonListener!!, this)
        }

        if (protectionListener == null) {
            protectionListener = FrameProtectionListener(this)
            server.pluginManager.registerEvents(protectionListener!!, this)
        }

        server.pluginManager.addPermission(Permission("mymap.main", "MyMap main command", "op"))
        server.commandMap.register("mymap", MainCommand("mymap", this))
    }

    override fun onDisable() {
        SettingListener.clearSettingDataMap()
    }

    fun removeFrame(frame: MyMapFrame): Boolean {
        val event = FrameRemoveEvent(frame)
        Server.getInstance().pluginManager.callEvent(event)
        if (event.isCancelled) {
            return false
        }
        frame.clear()
        list!!.remove(frame)
        return true
    }

    fun addFrame(frame: MyMapFrame): Boolean {
        Objects.requireNonNull(frame, "frame")
        val event = FrameAddEvent(frame)
        Server.getInstance().pluginManager.callEvent(event)
        if (event.isCancelled) {
            return false
        }

        if (!list!!.add(frame)) {
            return false
        }

        frame.save()
        return true
    }

    companion object {
        lateinit var instance: MyMap
    }
}
