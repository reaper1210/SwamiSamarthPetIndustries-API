package com.swamisamarthpet.routes

import com.swamisamarthpet.API_VERSION
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Route.downloadImage(){
    post("$API_VERSION/downloadImage"){
        val parameters = call.receive<Parameters>()
        val filename = parameters["name"]!!
        val file = File("./src/main/resources/static/uploads/$filename")
        if(file.exists()) {
            call.respond(file.readBytes())
        }
        else call.respond(HttpStatusCode.NotFound)
    }
}