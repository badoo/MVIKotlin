package com.arkivanov.mvikotlin.extensions.androidx.instancekeeper

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.core.instancekeeper.DefaultInstanceKeeper
import com.arkivanov.mvikotlin.core.instancekeeper.ExperimentalInstanceKeeperApi

@ExperimentalInstanceKeeperApi
internal class InstanceKeeperViewModel : ViewModel() {

    val instanceKeeper = DefaultInstanceKeeper()

    override fun onCleared() {
        instanceKeeper.destroy()

        super.onCleared()
    }
}
