package com.arkivanov.mvikotlin.timetravel.client

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array

fun ArrayBuffer.toByteArray(): ByteArray = Int8Array(this).unsafeCast<ByteArray>()
