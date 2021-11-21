package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.model.Member
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*

fun Route.connectUser(){
    webSocket("/$API_VERSION/connectUser") {
        val sessionId = generateSessionId()
        val socket = this
        val userId = call.parameters["userId"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing User Id")
        SupportRepo().connectUser(userId,sessionId,socket)
    }
}