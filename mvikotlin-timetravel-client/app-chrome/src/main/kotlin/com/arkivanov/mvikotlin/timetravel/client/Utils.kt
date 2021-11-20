package com.arkivanov.mvikotlin.timetravel.client

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

fun ArrayBuffer.stringify(): String {
    val byteArray = Int8Array(this)
    val array = Array(byteArray.length) { byteArray[it] }

    return JSON.stringify(array)
}

fun String.parseArrayBuffer(): ArrayBuffer {
    val array = JSON.parse<Array<Byte>>(this)
    val arrayBuffer = ArrayBuffer(array.size)
    val byteArray = Int8Array(arrayBuffer)

    array.forEachIndexed { index, byte ->
        byteArray[index] = byte
    }

    return arrayBuffer
}
