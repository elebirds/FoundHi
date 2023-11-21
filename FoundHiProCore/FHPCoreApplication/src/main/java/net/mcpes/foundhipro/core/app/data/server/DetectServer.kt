package net.mcpes.foundhipro.core.app.data.server

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.ServerType

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/17
 * version 1.0
 */
class DetectServer(id: String, name: String, serverIP: String, serverPort: Int, channel: Channel) : ProvenServer(id, name, serverIP, serverPort, channel, ServerType.ServerType.DETECT)