package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateUnreads(){
    post("$API_VERSION/updateUnreads"){
        val parameters = call.receive<Parameters>()
        val userId = parameters["userId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing User's Name")
        val isUserOrAdmin = parameters["isUserOrAdmin"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing isUser")
        try{
            val result = SupportRepo().updateUserUnread(userId,isUserOrAdmin)
            call.respond(HttpStatusCode.OK,result)
        }catch (e:Throwable){
            call.respond(HttpStatusCode.InternalServerError,e.message.toString())
        }

    }
}