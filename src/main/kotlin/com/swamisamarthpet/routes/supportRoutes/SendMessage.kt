package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.Constants
import com.swamisamarthpet.data.model.Member
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
        val userId = call.parameters["userId"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing User Id")
        val message = call.parameters["message"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing Message")
        val dateAndTime = call.parameters["dateAndTime"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing Date And Time")
        val messageFrom = call.parameters["messageFrom"]?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Missing Message From")
        val member = Constants.socketHashMap[userId] ?: return@webSocket call.respond(HttpStatusCode.Unauthorized,"Socket Not Found")

        try{
            SupportRepo().sendMessage(userId, message, dateAndTime, messageFrom, member)
        }
        catch (e:Throwable){
            call.respondText(e.message.toString())
        }

    }
}