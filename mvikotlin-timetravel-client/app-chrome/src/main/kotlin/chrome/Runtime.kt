@file:JsQualifier("chrome")

package chrome

external val runtime: Runtime

external interface Runtime {
    val lastError: LastError
    val onConnectExternal: OnConnectExternal
}

external interface LastError {
    val message: String
}

external interface OnConnectExternal {
    fun addListener(callback: (Port) -> Unit)
}

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
