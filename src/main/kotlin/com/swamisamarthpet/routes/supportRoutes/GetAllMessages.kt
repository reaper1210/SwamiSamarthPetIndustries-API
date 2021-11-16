package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllMessages(){
    post("$API_VERSION/getAllMessages"){
        val parameters = call.receive<Parameters>()
        val userId = parameters["userId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing User Id")
        try{
            val result = SupportRepo().getAllMessages(userId)
            call.respond(HttpStatusCode.OK,result)
        }
        catch (e:Throwable){
            call.respondText(e.message.toString())
        }
    }
}