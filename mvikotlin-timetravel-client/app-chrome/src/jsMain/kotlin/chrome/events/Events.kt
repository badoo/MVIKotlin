package chrome.events

external interface Event<out T : Any> {

    fun addListener(callback: (T) -> Unit)
}
