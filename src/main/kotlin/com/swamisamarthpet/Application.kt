package com.swamisamarthpet

import io.ktor.application.*
import com.swamisamarthpet.plugins.*
import io.ktor.websocket.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

const val API_VERSION = "v1"

@Suppress("unused")
fun Application.module() {

    DatabaseFactory.init()

    install(WebSockets)

    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureDoubleReceive(this)

}
