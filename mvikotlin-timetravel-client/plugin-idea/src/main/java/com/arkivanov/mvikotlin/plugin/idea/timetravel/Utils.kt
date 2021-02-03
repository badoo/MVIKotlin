package com.arkivanov.mvikotlin.plugin.idea.timetravel

import com.intellij.openapi.ui.Messages

internal fun showErrorDialog(text: String) {
    Messages.showErrorDialog(text, "MVIKotlin")
}
