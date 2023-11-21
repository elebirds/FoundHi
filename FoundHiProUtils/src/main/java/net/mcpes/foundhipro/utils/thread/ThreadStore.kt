package net.mcpes.foundhipro.utils.thread

import java.util.concurrent.ConcurrentHashMap

object ThreadStore {
    val store: Map<String, Any> = ConcurrentHashMap()
}
