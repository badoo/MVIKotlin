package com.arkivanov.mvikotlin.extensions.androidx.statekeeper

import android.os.Bundle
import androidx.savedstate.SavedStateRegistry
import com.arkivanov.mvikotlin.core.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.mvikotlin.core.statekeeper.StateKeeper
import kotlin.reflect.KClass

@ExperimentalStateKeeperApi
internal abstract class AndroidStateKeeper<T : Any>(
    private val registry: SavedStateRegistry,
    private val clazz: KClass<out T>,
    private val key: String
) : StateKeeper<T> {

    override fun consume(): T? =
        registry
            .consumeRestoredStateForKey(key)
            ?.apply { classLoader = clazz.java.classLoader }
            ?.getValue(KEY)

    override fun getState(): T? = consume()

    override fun setSupplier(supplier: (() -> T)?) {
        if (supplier == null) {
            registry.unregisterSavedStateProvider(key)
        } else {
            registry.registerSavedStateProvider(key) { saveState(supplier) }
        }
    }

    private fun saveState(supplier: () -> T): Bundle =
        Bundle().apply {
            putValue(KEY, supplier())
        }

    abstract fun <S : T> Bundle.getValue(key: String): S?

    abstract fun Bundle.putValue(key: String, value: T)

    private companion object {
        private const val KEY = "key"
    }
}
