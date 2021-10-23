package com.swamisamarthpet.routes.machineRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.MachineRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getMachineById(){

    post("$API_VERSION/getMachineById"){

        val parameters = call.receive<Parameters>()
        val machineId = parameters["machineId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Category Name")

        try{
            val result = MachineRepo(categoryName).getMachineById(machineId.toInt())
            call.respond(HttpStatusCode.OK,result!!)
        }catch (e: Throwable){
            call.respond(e.message.toString())
        }

    }

}