@file:JsQualifier("chrome.sockets.tcp")

package chrome.sockets.tcp

import chrome.events.Event
import org.khronos.webgl.ArrayBuffer

external val onReceive: Event<ReceiveInfo>

external interface ReceiveInfo {
    val data: ArrayBuffer
    val socketId: Int
}

external fun send(
    sockedId: Int,
    data: ByteArray,
    callback: (SendInfo) -> Unit
)

external interface SendInfo {
    val bufferSize: Int
    val connected: Boolean
    val localAddress: String?
    val localPort: String?
    val name: String?
    val paused: Boolean
    val peerAddress: String?
    val peerPort: String?
    val persistent: Boolean
    val socketId: Int
}

external fun setPaused(
    socketId: Int,
    paused: Boolean,
    callback: () -> Unit = definedExternally,
)
