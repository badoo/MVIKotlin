package com.arkivanov.mvikotlin.core.statekeeper

import kotlin.js.JsName

@JsName("simpleStateKeeperContainer")
@Suppress("FunctionName") // Factory function
fun SimpleStateKeeperContainer(): SimpleStateKeeperContainer =
    object : SimpleStateKeeperContainer, StateKeeperContainer<MutableMap<String, Any>, Any> by StateKeeperContainer(
        get = { map, key, _ -> map[key] },
        put = { map, key, _, value -> map[key] = value }
    ) {
    }
