package com.arkivanov.mvikotlin.core.statekeeper

import kotlin.reflect.KClass

/**
 * Represents a provider of [StateKeeper]s
 */
interface StateKeeperProvider<in T : Any> {

    /**
     * Provides instances of [StateKeeper] by key
     *
     * @param key a string key, must be unique within the provider instance
     * @return an instance of the [StateKeeper]
     */
    fun <S : T> get(key: String, clazz: KClass<out S>): StateKeeper<S>
}
