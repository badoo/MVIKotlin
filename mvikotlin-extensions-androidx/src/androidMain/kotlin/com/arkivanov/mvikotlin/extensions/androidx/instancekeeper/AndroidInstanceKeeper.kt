package com.arkivanov.mvikotlin.extensions.androidx.instancekeeper

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.arkivanov.mvikotlin.core.instancekeeper.ExperimentalInstanceKeeperApi
import com.arkivanov.mvikotlin.core.instancekeeper.InstanceKeeper

@ExperimentalInstanceKeeperApi
fun <T : ViewModelStoreOwner> T.getInstanceKeeper(): InstanceKeeper =
    ViewModelProvider(this)
        .get(InstanceKeeperViewModel::class.java)
        .instanceKeeper
