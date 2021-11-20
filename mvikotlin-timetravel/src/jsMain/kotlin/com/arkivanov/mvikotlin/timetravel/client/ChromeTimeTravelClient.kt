package com.arkivanov.mvikotlin.timetravel.client

import chrome.Port
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.observer
import com.arkivanov.mvikotlin.timetravel.controller.TimeTravelController
import com.arkivanov.mvikotlin.timetravel.controller.timeTravelController
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.ProtoObject
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetravelcomand.TimeTravelCommand
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetraveleventvalue.TimeTravelEventValue
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.value.ValueParser
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoDecoder
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoEncoder
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoFrameDecoder
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoFrameEncoder
import com.arkivanov.mvikotlin.timetravel.server.StateDiff

class ChromeTimeTravelClient(
    private val controller: TimeTravelController = timeTravelController
) {

    private var port: Port? = null
    private var disposable: Disposable? = null

    fun start() {
        if (port != null) {
            return
        }

        val port = chrome.runtime.connect(extensionId = EXTENSION_ID)
        this.port = port

        val protoDecoder = ProtoDecoder()
        val protoFrameDecoder = ProtoFrameDecoder {
            console.log("protoFrameDecoder")
            console.log(it)
            onCommandReceived(protoDecoder.decode(it), port)
        }

        port.onMessage.addListener { message, _ ->
            console.log("onMessage")
            console.log(message)
            val arr = message.unsafeCast<String>().parseByteArray()
            protoFrameDecoder.accept(arr)
        }

        val stateDiff = StateDiff()
        disposable = controller.states(observer { sendData(protoObject = stateDiff(it), port = port) })

        port.onDisconnect.addListener {
            disposable?.dispose()
            disposable = null
            this.port = null
        }
    }

    private fun String.parseByteArray(): ByteArray {
        val array = JSON.parse<Array<Byte>>(this)

        return ByteArray(array.size) { array[it] }
    }

    private fun onCommandReceived(command: ProtoObject, port: Port) {
        console.log("onCommandReceived")
        console.log(command)

        when (command) {
            is TimeTravelCommand.StartRecording -> controller.startRecording()
            is TimeTravelCommand.StopRecording -> controller.stopRecording()
            is TimeTravelCommand.MoveToStart -> controller.moveToStart()
            is TimeTravelCommand.StepBackward -> controller.stepBackward()
            is TimeTravelCommand.StepForward -> controller.stepForward()
            is TimeTravelCommand.MoveToEnd -> controller.moveToEnd()
            is TimeTravelCommand.Cancel -> controller.cancel()
            is TimeTravelCommand.DebugEvent -> controller.debugEvent(eventId = command.eventId)
            is TimeTravelCommand.AnalyzeEvent -> analyzeEvent(eventId = command.eventId, port = port)
            is TimeTravelCommand.ExportEvents -> Unit // TODO: implement
            is TimeTravelCommand.ImportEvents -> Unit // TODO: implement
            else -> Unit
        }.let {}
    }

    private fun analyzeEvent(eventId: Long, port: Port) {
        val event = controller.state.events.firstOrNull { it.id == eventId } ?: return
        val parsedValue = ValueParser().parseValue(event.value)
        sendData(protoObject = TimeTravelEventValue(eventId = eventId, value = parsedValue), port = port)
    }

    private fun sendData(protoObject: ProtoObject, port: Port) {
        val frameEncoder = ProtoFrameEncoder { data, size -> port.postMessage(data.stringify(size)) }
        val protoEncoder = ProtoEncoder(frameEncoder::accept)
        protoEncoder.encode(protoObject)
    }

    private fun ByteArray.stringify(size: Int): String =
        JSON.stringify(Array(size) { get(it) })

    fun stop() {
        disposable?.dispose()
        disposable = null
        port?.disconnect()
        port = null
    }

    private companion object {
        private const val EXTENSION_ID = "ekffenjemdhlffjmedmmndlmhfgmcadj"
    }
}
