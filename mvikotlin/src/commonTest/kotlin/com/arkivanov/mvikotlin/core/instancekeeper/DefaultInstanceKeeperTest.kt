package com.arkivanov.mvikotlin.core.instancekeeper

import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DefaultInstanceKeeperTest {

    @Test
    fun retains_instances() {
        val container = DefaultInstanceKeeper()
        val data = Data()

        container.getOrCreate(key = "data") { data }
        val retainedData = container.getOrCreate<Data>(key = "data") { throw IllegalStateException() }

        assertSame(data, retainedData)
    }

    @Test
    fun destroys_instances() {
        val container = DefaultInstanceKeeper()
        val data = List(10) { Data() }

        data.forEachIndexed { index, data ->
            container.getOrCreate(index) { data }
        }

        container.destroy()

        data.forEach {
            assertTrue(it.isDestroyed)
        }
    }

    private class Data : InstanceKeeper.Instance {
        var isDestroyed: Boolean = false
            private set

        override fun onDestroy() {
            isDestroyed = true
        }
    }
}
