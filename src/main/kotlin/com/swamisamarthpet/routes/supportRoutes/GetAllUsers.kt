package com.swamisamarthpet.routes.supportRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllUsers(){
    get("$API_VERSION/getAllUsers"){
        try{
            val result = SupportRepo().getAllUsers()
            call.respond(HttpStatusCode.OK, result)
        }catch(e: Throwable){
            call.respond(e.message.toString())
        }
    }
}