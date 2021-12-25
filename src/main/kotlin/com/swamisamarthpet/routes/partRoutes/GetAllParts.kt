package com.swamisamarthpet.routes.partRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.PartRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllParts(){

    post("$API_VERSION/getAllParts"){
        val parameters = call.receive<Parameters>()
        val machineName = parameters["machineName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Machine Name")

        try{
            val result = PartRepo(machineName).getAllParts()
            call.respond(HttpStatusCode.OK,result)
        }catch (e: Throwable){
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }

    }

}