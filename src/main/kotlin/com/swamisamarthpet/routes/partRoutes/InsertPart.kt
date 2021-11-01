package com.swamisamarthpet.routes.partRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.PartRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.io.File

fun Route.insertPart(){
    post("$API_VERSION/insertPart"){
        val parameters = call.receive<Parameters>()
        val multiPart = call.receiveMultipart()
        val partName = parameters["partName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Name")
        val partDetails = parameters["partDetails"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Details")
        val machineName = parameters["machineName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Machine Name")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{

                val result = PartRepo(machineName).insertPart(partName,multiPart,partDetails,machineName)
                call.respond(HttpStatusCode.OK,result)

            }catch (e: Throwable){
                call.respond(e.message.toString())
            }
        }
        else{
            call.respond("Invalid Password")
        }


    }
}