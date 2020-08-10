@file:JvmName("MainThreadAssert")

package com.arkivanov.mvikotlin.utils.internal

import android.os.Looper

private fun getMainLooper(): Looper? =
    try {
        Looper.getMainLooper()
    } catch (e: Exception) {
        null
    }

private val mainThreadId: Long by lazy {
    var mainThreadId: Long? = getMainLooper()?.thread?.id
    if (mainThreadId == null) {
        val currentThread = Thread.currentThread()
        mainThreadId = currentThread.id
        System.err.println("Error getting Main Looper, current thread is considered as main: $currentThread")
    }

    mainThreadId!!
}

internal actual val isMainThread: Boolean get() = Thread.currentThread().id == mainThreadId

internal actual val currentThreadDescription: String get() = Thread.currentThread().name
