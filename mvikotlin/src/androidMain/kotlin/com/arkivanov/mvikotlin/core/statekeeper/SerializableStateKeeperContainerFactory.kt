package com.arkivanov.mvikotlin.core.statekeeper

import android.os.Bundle
import java.io.Serializable

@Suppress("FunctionName") // Factory function
fun SerializableStateKeeperContainer(): SerializableStateKeeperContainer =
    object : SerializableStateKeeperContainer, StateKeeperContainer<Bundle, Serializable> by StateKeeperContainer(
        get = { bundle, key, clazz -> bundle.getSafe(key, clazz, Bundle::getSerializable) },
        put = { bundle, key, _, value -> bundle.putSafe(key, value, Bundle::putSerializable) }
    ) {
    }
