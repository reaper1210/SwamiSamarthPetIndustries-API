package com.swamisamarthpet.routes.partRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.PartRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.deletePart(){
    post("$API_VERSION/deletePart"){
        val parameters = call.receive<Parameters>()
        val partId = parameters["partId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")
        val machineName = parameters["machineName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Machine Name")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                val result = PartRepo(machineName).deletePart(partId.toInt())
                call.respond(HttpStatusCode.OK,result)
            }catch(e: Throwable){
                call.respond(e.message.toString())
            }
        }
        else{
            call.respond("Invalid Password")
        }

    }
}