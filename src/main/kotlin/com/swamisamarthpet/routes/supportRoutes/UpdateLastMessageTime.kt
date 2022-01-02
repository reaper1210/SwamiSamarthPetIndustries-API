package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateLastMessageTime(){
    post("$API_VERSION/updateLastMessageTime"){
        val parameters = call.receive<Parameters>()
        val userId = parameters["userId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing User Id")
        val lastMessageTime = parameters["lastMessageTime"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Time")

        try{
            val result = SupportRepo().updateUserLastMessageTime(userId,lastMessageTime)
            call.respond(HttpStatusCode.OK,result)
        }catch (e:Throwable){
            call.respond(HttpStatusCode.InternalServerError,e.message.toString())
        }

    }
}