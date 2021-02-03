package com.arkivanov.mvikotlin.plugin.idea.timetravel

import androidx.compose.desktop.ComposePanel
import androidx.compose.desktop.DesktopTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.timetravel.client.internal.client.TimeTravelClient
import com.arkivanov.mvikotlin.timetravel.client.internal.client.adbcontroller.DefaultAdbController
import com.arkivanov.mvikotlin.timetravel.client.internal.client.integration.TimeTravelClientComponent
import com.arkivanov.mvikotlin.timetravel.client.internal.client.ui.TimeTravelClientUi
import com.arkivanov.mvikotlin.timetravel.client.internal.compose.ToolbarButtonConfig
import com.arkivanov.mvikotlin.timetravel.client.internal.compose.UiConfig
import com.arkivanov.mvikotlin.timetravel.client.internal.compose.theme.TimeTravelClientTheme
import com.arkivanov.mvikotlin.timetravel.client.internal.settings.SettingsConfig
import com.intellij.openapi.fileChooser.FileChooser.chooseFile
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory.createSingleFileDescriptor
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.russhwolf.settings.JvmPreferencesSettings
import org.apache.commons.lang.SystemUtils.getUserHome
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.prefs.Preferences
import javax.swing.JComponent

class TimeTravelWindow(
    private val project: Project
) {

    fun getContent(): JComponent {
        val client = client()

        return ComposePanel().apply {
            setContent {
                Content(client)
            }
        }
    }

    @Composable
    private fun Content(client: TimeTravelClient) {
        val swingColors by rememberSwingColors()

        TimeTravelClientTheme(
            isDarkMode = swingColors.isDarkMode,
            uiConfig = UiConfig(
                toolbarButtonConfig = ToolbarButtonConfig(
                    buttonModifier = Modifier.padding(2.dp).size(24.dp),
                    iconModifier = Modifier.size(16.dp)
                )
            ),
            colorsOverride = swingColors.getOverrideFunc()
        ) {
            DesktopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TimeTravelClientUi(client)
                }
            }
        }
    }

    private fun client(): TimeTravelClient {
        val preferencesFactory = JvmPreferencesSettings.Factory(Preferences.userNodeForPackage(TimeTravelToolWindowFactory::class.java))

        return TimeTravelClientComponent(
            lifecycle = TimeTravelToolWindowListener.getLifecycle(),
            storeFactory = DefaultStoreFactory(),
            settingsFactory = preferencesFactory,
            settingsConfig = SettingsConfig(
                defaults = SettingsConfig.Defaults(
                    connectViaAdb = true
                ),
                editing = SettingsConfig.Editing(
                    isDarkModeEnabled = false
                )
            ),
            adbController = DefaultAdbController(
                settingsFactory = preferencesFactory,
                selectAdbPath = ::selectAdbPath
            ),
            onImportEvents = ::importEvents,
            onExportEvents = ::exportEvents
        )
    }

    private fun importEvents(): ByteArray? {
        val path = chooseFile(createSingleFileDescriptor("tte").withTitle("Select file"), project, null)?.path ?: return null

        return try {
            FileInputStream(path).use(FileInputStream::readBytes)
        } catch (e: IOException) {
            showErrorDialog("Error reading file: ${e.message}")
            null
        }
    }

    private fun exportEvents(data: ByteArray) {
        var path =
            FileChooserFactory
                .getInstance()
                .createSaveFileDialog(FileSaverDescriptor("Save file", "MVIKotlin time travel export", "tte"), project)
                .save(null, null)
                ?.file
                ?.absolutePath
                ?: return

        if (!path.endsWith(".tte")) {
            path += ".tte"
        }

        try {
            FileOutputStream(path).use { output ->
                output.write(data)
                output.flush()
            }
        } catch (e: IOException) {
            showErrorDialog("Error reading file: ${e.message}")
        }
    }

    private fun selectAdbPath(): String? =
        chooseFile(
            createSingleFileDescriptor()
                .withFileFilter { it.name == "adb" }
                .withTitle("Select ADB executable"),
            project,
            getUserHome()
                .takeIf(File::exists)
                ?.let { LocalFileSystem.getInstance().findFileByIoFile(it) }
        )?.path
}
