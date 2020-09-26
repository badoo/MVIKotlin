package com.arkivanov.mvikotlin.core.instancekeeper

import com.arkivanov.mvikotlin.core.store.Store

/**
 * Same as [InstanceKeeper.getOrCreate] but the key is `T::class`
 */
@ExperimentalInstanceKeeperApi
inline fun <reified T : InstanceKeeper.Instance> InstanceKeeper.getOrCreate(noinline factory: () -> T): T =
    getOrCreate(T::class, factory)

/**
 * Either returns a currently retained [Store] instance or creates (and retains) a new one.
 * The retained [Store] is automatically disposed at the end of the [InstanceKeeper]'s lifecycle.
 *
 * See [InstanceKeeper] for more information.
 *
 * @param factory a factory function, called when there is no retained instance yet
 * @return either a currently retained [Store] instance or a new one
 */
@ExperimentalInstanceKeeperApi
fun <T : Store<*, *, *>> InstanceKeeper.getOrCreateStore(key: Any, factory: () -> T): T =
    getOrCreate(key) { RetainedStoreInstance(factory()) }
        .store

/**
 * Same as [getOrCreateStore] but the key is `T::class`
 */
@ExperimentalInstanceKeeperApi
inline fun <reified T : Store<*, *, *>> InstanceKeeper.getOrCreateStore(noinline factory: () -> T): T =
    getOrCreateStore(T::class, factory)

private class RetainedStoreInstance<T : Store<*, *, *>>(
    val store: T
) : InstanceKeeper.Instance {

    override fun onDestroy() {
        store.dispose()
    }
}
