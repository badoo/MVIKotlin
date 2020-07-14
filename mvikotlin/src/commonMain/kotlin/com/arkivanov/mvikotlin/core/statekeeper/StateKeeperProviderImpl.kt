package com.arkivanov.mvikotlin.core.statekeeper

import kotlin.reflect.KClass

internal class StateKeeperProviderImpl<State : Any, in T : Any>(
    private val savedState: State?,
    private val get: (state: State, key: String, clazz: KClass<out T>) -> T?,
    private val register: (key: String, clazz: KClass<out T>, supplier: () -> T) -> Unit
) : StateKeeperProvider<T> {

    override fun <S : T> get(key: String, clazz: KClass<out S>): StateKeeper<S> =
        object : StateKeeper<S> {
            @Suppress("UNCHECKED_CAST")
            override val state: S?
                get() = savedState?.let { get(it, key, clazz) } as S?

            override fun register(supplier: () -> S) {
                register(key, clazz, supplier)
            }
        }
}
