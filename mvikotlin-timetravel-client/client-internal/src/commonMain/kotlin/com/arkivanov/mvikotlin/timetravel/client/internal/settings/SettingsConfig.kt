package com.arkivanov.mvikotlin.timetravel.client.internal.settings

class SettingsConfig(
    val defaults: Defaults,
    val editing: Editing
) {
    class Defaults(
        val connectViaAdb: Boolean
    )

    class Editing(
        val isDarkModeEnabled: Boolean
    )
}
