package net.mcpes.foundhipro.core.network.protocol

enum class ProtocolInfo constructor(var pid: Int) {
    SERVER_CONNECT_PACKET(0),
    PING_PONG_PACKET(1),
    SET_MAX_REQUEST_COUNT_PACKET(2)
}