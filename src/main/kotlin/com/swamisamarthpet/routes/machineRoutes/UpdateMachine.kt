package com.swamisamarthpet.routes.machineRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.MachineRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateMachine(){

    post("$API_VERSION/updateMachine"){

        val parameters = call.receive<Parameters>()
        val multiPart = call.receiveMultipart()
        val machineId = parameters["machineId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")
        val machineDetails = parameters["machineDetails"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Details")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Category Name")
        val youtubeVideoLink = parameters["youtubeVideoLink"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Youtube Video Link")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                val result = MachineRepo(categoryName).updateMachine(machineId.toInt(),multiPart,machineDetails,youtubeVideoLink)
                call.respond(HttpStatusCode.OK,result)
            }
            catch (e: Throwable){
                call.respond(HttpStatusCode.InternalServerError, e.message.toString())
            }
        }
        else call.respond(HttpStatusCode.Unauthorized,"Invalid Password")

    }

}