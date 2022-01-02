package com.swamisamarthpet.routes.machineRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.MachineRepo
import com.swamisamarthpet.data.tables.PartTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionScope
import java.io.File

fun Route.insertMachine(){

    post("$API_VERSION/insertMachine"){

        val parameters = call.receiveParameters()
        val multiPart = call.receiveMultipart()
        val machineName = parameters["machineName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Name")
        val machineDetails = parameters["machineDetails"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Details")
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Category Name")
        val youtubeVideoLink = parameters["youtubeVideoLink"]?: ""
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                val result = MachineRepo(categoryName).insertMachine(machineName,multiPart,machineDetails,youtubeVideoLink)
                call.respond(HttpStatusCode.OK,result)
            }catch (e:Throwable){
                call.respond(HttpStatusCode.InternalServerError, e.message.toString())
            }
        }
        else call.respond(HttpStatusCode.Unauthorized,"Invalid Password")

    }

}