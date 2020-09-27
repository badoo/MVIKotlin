package com.arkivanov.mvikotlin.extensions.androidx.statekeeper

import android.os.Parcelable
import androidx.savedstate.SavedStateRegistryOwner
import com.arkivanov.mvikotlin.core.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.mvikotlin.core.statekeeper.StateKeeper
import com.arkivanov.mvikotlin.core.statekeeper.StateKeeperRegistry
import kotlin.reflect.KClass

@ExperimentalStateKeeperApi
fun SavedStateRegistryOwner.getParcelableStateKeeperRegistry(): StateKeeperRegistry<Parcelable> =
    object : StateKeeperRegistry<Parcelable> {
        override fun <S : Parcelable> get(clazz: KClass<out S>, key: String): StateKeeper<S> =
            ParcelableStateKeeper(registry = savedStateRegistry, clazz = clazz, key = key)
    }
