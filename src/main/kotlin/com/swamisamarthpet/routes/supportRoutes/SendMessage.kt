package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.sendMessage(){
    post("$API_VERSION/sendMessage"){
        val parameters = call.receive<Parameters>()
        val userId = parameters["userId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing User Id")
        val message = parameters["message"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Message")
        val dateAndTime = parameters["dateAndTime"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Date And Time")
        val messageFrom = parameters["messageFrom"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Message From")

        try{
            val result = SupportRepo().sendMessage(userId, message, dateAndTime, messageFrom)
            call.respond(HttpStatusCode.OK,result)
        }
        catch (e:Throwable){
            call.respondText(e.message.toString())
        }

    }
}