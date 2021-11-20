@file:JsQualifier("chrome")

package chrome

external val runtime: Runtime

external interface Runtime {
    fun connect(extensionId: String?, connectInfo: ConnectInfo? = definedExternally): Port
}

external interface ConnectInfo

external interface Port {
    val name: String
    val onDisconnect: OnDisconnect
    val onMessage: OnMessage

    fun disconnect()
    fun postMessage(message: Any)
}

external interface OnDisconnect {
    fun addListener(callback: (Port) -> Unit)
}

external interface OnMessage {
    fun addListener(callback: (message: Any, Port) -> Unit)
}
