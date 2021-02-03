package com.arkivanov.mvikotlin.timetravel.client.desktop

import androidx.compose.ui.unit.IntSize
import java.awt.Dimension
import java.awt.FileDialog
import java.awt.Toolkit
import java.io.File
import javax.swing.SwingUtilities

fun <T> invokeOnAwtSync(block: () -> T): T {
    var result: T? = null
    SwingUtilities.invokeAndWait { result = block() }

    @Suppress("UNCHECKED_CAST")
    return result as T
}

fun getPreferredWindowSize(desiredWidth: Int, desiredHeight: Int): IntSize {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    val preferredWidth: Int = (screenSize.width * 0.8f).toInt()
    val preferredHeight: Int = (screenSize.height * 0.8f).toInt()
    val width: Int = if (desiredWidth < preferredWidth) desiredWidth else preferredWidth
    val height: Int = if (desiredHeight < preferredHeight) desiredHeight else preferredHeight

    return IntSize(width, height)
}

val FileDialog.selectedFile: File?
    get() = if ((directory != null) && (file != null)) File(directory, file) else null
