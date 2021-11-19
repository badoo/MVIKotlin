@file:JsQualifier("chrome.sockets.tcpServer")

package chrome.sockets.tcpServer

import chrome.events.Event

external val onAccept: Event<AcceptInfo>

external fun create(
    properties: SocketProperties = definedExternally,
    callback: (CreateInfo) -> Unit
)

external fun listen(
    socketId: Int,
    address: String,
    port: Int,
    backlog: Int = definedExternally,
    callback: (result: Int) -> Unit
)

external class SocketProperties {
    val name: String = definedExternally
    val persistent: Boolean = definedExternally
}

external interface CreateInfo {
    val socketId: Int
}

external interface AcceptInfo {
    val clientSocketId: Int
    val socketId: Int
}
