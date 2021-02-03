package com.arkivanov.mvikotlin.timetravel.client.internal.utils

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.mvikotlin.core.lifecycle.DefaultLifecycleCallbacks
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.observer

internal fun <T : Any, S : Any> Store<*, T, *>.subscribeAsState(lifecycle: Lifecycle, mapper: (T) -> S): State<S> {
    val mutableState = mutableStateOf(mapper(state))

    lifecycle.subscribe(
        object : DefaultLifecycleCallbacks {
            private var disposable: Disposable? = null

            override fun onCreate() {
                disposable = states(observer { mutableState.value = mapper(it) })
            }

            override fun onDestroy() {
                disposable?.dispose()
                disposable = null
            }
        }
    )

    return mutableState
}
