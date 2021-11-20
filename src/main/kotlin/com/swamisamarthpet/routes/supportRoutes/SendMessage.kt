package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*

fun Route.sendMessage(){
    webSocket("$API_VERSION/sendMessage"){
        val sessionId = generateSessionId()
        val socket = this
        val parameters = call.receive<Parameters>()
        val userId = parameters["userId"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing User Id")
        val message = parameters["message"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing Message")
        val dateAndTime = parameters["dateAndTime"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing Date And Time")
        val messageFrom = parameters["messageFrom"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing Message From")

        try{
            val member = SupportRepo().onJoin(userId,sessionId,socket)
            val result = SupportRepo().sendMessage(userId, message, dateAndTime, messageFrom,member)
            call.respond(HttpStatusCode.OK,result)
        }
        catch (e:Throwable){
            call.respondText(e.message.toString())
        }

    }
}