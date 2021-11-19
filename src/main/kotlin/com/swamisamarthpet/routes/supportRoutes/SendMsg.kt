package com.swamisamarthpet.routes.supportRoutes

import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Route.sendMsg(){
    webSocket("/chat") {
        send("You are connected!")
        for(frame in incoming) {
            frame as? Frame.Text ?: continue
            val receivedText = frame.readText()
            send("You said: $receivedText")
        }
    }
}