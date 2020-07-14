package com.arkivanov.mvikotlin.core.statekeeper

import com.badoo.reaktive.utils.ensureNeverFrozen
import kotlin.reflect.KClass

internal class StateKeeperContainerImpl<in State : Any, in T : Any>(
    private val get: (state: State, key: String, clazz: KClass<out T>) -> T?,
    private val put: (state: State, key: String, clazz: KClass<out T>, value: T) -> Unit
) : StateKeeperContainer<State, T> {

    init {
        ensureNeverFrozen()
    }

    private val suppliers = HashMap<String, Pair<KClass<out T>, () -> T>>()

    override fun getProvider(savedState: State?): StateKeeperProvider<T> =
        StateKeeperProviderImpl(
            savedState = savedState,
            get = get,
            register = { key, clazz, supplier ->
                check(key !in suppliers) { "The supplier is already register with this key: $key" }
                suppliers[key] = clazz to supplier
            }
        )

    override fun save(outState: State) {
        suppliers.forEach { (key, classAndSupplier) ->
            put(outState, key, classAndSupplier.first, classAndSupplier.second())
        }
    }
}
