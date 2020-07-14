package com.arkivanov.mvikotlin.core.statekeeper

import android.os.Bundle
import android.os.Parcelable

@Suppress("FunctionName") // Factory function
fun ParcelableStateKeeperContainer(): ParcelableStateKeeperContainer =
    object : ParcelableStateKeeperContainer, StateKeeperContainer<Bundle, Parcelable> by StateKeeperContainer(
        get = { bundle, key, clazz -> bundle.getSafe(key, clazz) { getParcelable(it) } },
        put = { bundle, key, _, value -> bundle.putSafe(key, value, Bundle::putParcelable) }
    ) {
    }
