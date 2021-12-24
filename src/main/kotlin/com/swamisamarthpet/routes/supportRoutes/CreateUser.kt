package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.postgresql.util.PSQLException

fun Route.createUser(){
    post("$API_VERSION/createUser"){
        val parameters = call.receive<Parameters>()
        val userName = parameters["userName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing User's Name")
        val phoneNumber = parameters["phoneNumber"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Phone Number")

        try{
            val result = SupportRepo().createUser(userName,phoneNumber)
            call.respond(HttpStatusCode.OK,result)
        }catch (e:Throwable){
            call.respondText(e.message.toString())
        }

    }
}