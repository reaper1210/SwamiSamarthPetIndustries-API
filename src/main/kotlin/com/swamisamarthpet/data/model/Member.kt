package com.swamisamarthpet.data.model

import io.ktor.http.cio.websocket.*

data class Member(
    val userId: String,
    val sessionId: String,
    val socket: WebSocketSession
)
