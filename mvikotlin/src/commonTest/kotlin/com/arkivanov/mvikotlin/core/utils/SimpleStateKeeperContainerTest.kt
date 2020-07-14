package com.arkivanov.mvikotlin.core.utils

import com.arkivanov.mvikotlin.core.statekeeper.SimpleStateKeeperContainer
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleStateKeeperContainerTest {

    @Test
    fun saves_and_restores_states() {
        val container = SimpleStateKeeperContainer()
        val provider = container.getProvider(savedState = null)
        provider.get("key1", String::class).register { "state1" }
        provider.get("key2", String::class).register { "state2" }

        val map = HashMap<String, Any>()
        container.save(map)

        val newContainer = SimpleStateKeeperContainer()
        val newProvider = newContainer.getProvider(savedState = map)
        val state1 = newProvider.get("key1", String::class).state
        val state2 = newProvider.get("key2", String::class).state

        assertEquals("state1", state1)
        assertEquals("state2", state2)
    }
}
