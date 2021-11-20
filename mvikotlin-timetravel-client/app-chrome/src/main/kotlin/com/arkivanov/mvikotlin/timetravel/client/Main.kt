package com.arkivanov.mvikotlin.timetravel.client

import chrome.Port

fun main() {
    console.log("Loaded")

    val clientSockedIds = HashSet<Int>()
    val ports = HashSet<Port>()

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

                clientSockedIds += acceptInfo.clientSocketId

                chrome.sockets.tcp.setPaused(socketId = acceptInfo.clientSocketId, paused = false)

                chrome.sockets.tcp.onReceiveError.addListener onReceiveError@{ receiveErrorInfo ->
                    if (receiveErrorInfo.socketId != acceptInfo.clientSocketId) {
                        return@onReceiveError
                    }

                    console.log("onReceiveError")
                    console.log(receiveErrorInfo)
                    clientSockedIds -= receiveErrorInfo.socketId
                }

                chrome.sockets.tcp.onReceive.addListener onReceive@{ receiveInfo ->
                    if (receiveInfo.socketId != acceptInfo.clientSocketId) {
                        return@onReceive
                    }

                    println("onReceive")
                    console.log(receiveInfo)

                    val str = receiveInfo.data.stringify()
                    ports.forEach { port ->
                        port.postMessage(str)
                    }
                }
            }
        }
    }

    chrome.runtime.onConnectExternal.addListener { port ->
        console.log("onConnectExternal")
        console.log(port)
        ports += port

        port.onDisconnect.addListener {
            console.log("onDisconnect")
            console.log(it)
            ports -= it
        }

        port.onMessage.addListener { message, _ ->
            console.log("onMessage")
            console.log(message)

            val data = message.unsafeCast<String>().parseArrayBuffer()

            clientSockedIds.forEach { clientSocketId ->
                chrome.sockets.tcp.send(
                    sockedId = clientSocketId,
                    data = data
                ) {}
            }
        }
    }
}
