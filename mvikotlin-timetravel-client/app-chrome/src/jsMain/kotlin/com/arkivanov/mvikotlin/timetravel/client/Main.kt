package com.arkivanov.mvikotlin.timetravel.client

import com.arkivanov.mvikotlin.timetravel.proto.internal.data.storeeventtype.StoreEventType
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetravelevent.TimeTravelEvent
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetraveleventsupdate.TimeTravelEventsUpdate
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetravelstateupdate.TimeTravelStateUpdate
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoDecoder
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoEncoder
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoFrameDecoder
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoFrameEncoder
import org.khronos.webgl.ArrayBuffer

fun main(args: Array<String>) {
    console.log("Loaded")
    chrome.sockets.tcpServer.create { createInfo ->
        console.log("Created")
        console.log(createInfo)

        chrome.sockets.tcpServer.listen(socketId = createInfo.socketId, address = "127.0.0.1", port = 6379) { result ->
            console.log("Listen: $result")

            if (result < 0) {
                console.log("Error listening: ${chrome.runtime.lastError.message}")
                return@listen
            }

            chrome.sockets.tcpServer.onAccept.addListener onAccept@{ acceptInfo ->
                if (acceptInfo.socketId != createInfo.socketId) {
                    return@onAccept
                }

                console.log("onAccept")
                console.log(acceptInfo)

                chrome.sockets.tcp.setPaused(socketId = acceptInfo.clientSocketId, paused = false)

                val protoDecoder = ProtoDecoder()

                val protoFrameDecoder =
                    ProtoFrameDecoder { data ->
                        val protoObject = protoDecoder.decode(data)
                        console.log(protoObject)
                    }

                chrome.sockets.tcp.onReceive.addListener onReceive@{ receiveInfo ->
                    if (receiveInfo.socketId != acceptInfo.clientSocketId) {
                        return@onReceive
                    }

                    println("onReceive")
                    console.log(receiveInfo)

                    protoFrameDecoder.accept(data = receiveInfo.data.toByteArray())
                }

                val state =
                    TimeTravelEventsUpdate.All(
                        listOf(
                            TimeTravelEvent(
                                id = 1L,
                                storeName = "SomeStore",
                                type = StoreEventType.STATE,
                                valueType = "String",
                            )
                        )
                    )

                val frameEncoder = ProtoFrameEncoder { data, size ->
                    chrome.sockets.tcp.send(
                        sockedId = acceptInfo.clientSocketId,
                        data = data.copyOf(size)
                    ) { sendInfo ->
                        console.log("send")
                        console.log(sendInfo)
                    }
                }
                val protoEncoder = ProtoEncoder(frameEncoder::accept)

                protoEncoder.encode(
                    TimeTravelStateUpdate(
                        eventsUpdate = state,
                        selectedEventIndex = 0,
                        mode = TimeTravelStateUpdate.Mode.STOPPED,
                    )
                )
            }
        }
    }
}


//fun EventTarget.onEvent(type: String, listener: (Event) -> Unit) =
//    addEventListener(type, listener)
//
//fun EventTarget.onContentLoadedEvent(listener: (Event) -> Unit) =
//    onEvent("DOMContentLoaded", listener)
