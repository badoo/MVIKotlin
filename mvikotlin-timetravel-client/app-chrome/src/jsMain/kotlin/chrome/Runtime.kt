@file:JsQualifier("chrome")

package chrome

external val runtime: Runtime

external interface Runtime {
    val lastError: LastError
}

external interface LastError {
    val message: String
}
