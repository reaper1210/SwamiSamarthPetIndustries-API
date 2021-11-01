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
        val machinePdf = parameters["machinePdf"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Pdf")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Category Name")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                val result = MachineRepo(categoryName).updateMachine(machineId.toInt(),multiPart,machineDetails,machinePdf)
                call.respond(HttpStatusCode.OK,result)
            }
            catch (e: Throwable){
                call.respond(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")

    }

}