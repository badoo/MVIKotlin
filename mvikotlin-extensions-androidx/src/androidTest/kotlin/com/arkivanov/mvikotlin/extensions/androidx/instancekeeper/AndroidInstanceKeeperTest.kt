package com.arkivanov.mvikotlin.extensions.androidx.instancekeeper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.arkivanov.mvikotlin.core.instancekeeper.InstanceKeeper
import org.junit.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue
import androidx.lifecycle.Lifecycle as AndroidLifecycle

class AndroidInstanceKeeperTest {

    @Test
    fun retains_instance_WHEN_recreated() {
        val viewModelStore = ViewModelStore()
        val instance = Data()
        Owner(viewModelStore).keeper.getOrCreate(key = "data") { instance }

        val retainedInstance = Owner(viewModelStore).keeper.getOrCreate<Data>(key = "data") { throw IllegalStateException() }

        assertSame(instance, retainedInstance)
    }

    @Test
    fun destroys_instances() {
        val viewModelStore = ViewModelStore()
        val owner = Owner(viewModelStore)
        val instances = List(10) { Data() }

        instances.forEachIndexed { index, data ->
            owner.keeper.getOrCreate(index) { data }
        }

        viewModelStore.clear()

        instances.forEach {
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

    private class Owner(
        private val viewModelStore: ViewModelStore
    ) : ViewModelStoreOwner, LifecycleOwner {
        private val lifecycle = LifecycleRegistry(this)
        val keeper = getInstanceKeeper()

        override fun getViewModelStore(): ViewModelStore = viewModelStore

        override fun getLifecycle(): AndroidLifecycle = lifecycle
    }
}
