package com.swamisamarthpet.routes.partRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.PartRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getPartById(){
    post("$API_VERSION/getPartById"){
        val parameters = call.receive<Parameters>()
        val partId = parameters["partId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")
        val machineName = parameters["machineName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing machine Name")

        try{
            val result = PartRepo(machineName).getPartById(partId.toInt())
            call.respond(HttpStatusCode.OK,result!!)
        }catch (e: Throwable){
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }

    }
}