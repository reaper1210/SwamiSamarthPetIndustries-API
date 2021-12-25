package com.swamisamarthpet.routes.machineRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.MachineRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllMachines(){
    post("$API_VERSION/getAllMachines"){
        val parameters = call.receive<Parameters>()
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Category Name")

        try{
            val result = MachineRepo(categoryName).getAllMachines()
            call.respond(HttpStatusCode.OK,result)
        }catch (e: Throwable){
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }

    }
}