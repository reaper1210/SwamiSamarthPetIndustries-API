package com.swamisamarthpet.routes.machineRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.MachineRepo
import com.swamisamarthpet.data.tables.PartTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionScope

fun Route.insertMachine(){

    post("$API_VERSION/insertMachine"){

        val parameters = call.receive<Parameters>()
        val machineName = parameters["machineName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Name")
        val machineImage = parameters["machineImage"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Image")
        val machineDetails = parameters["machineDetails"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Details")
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Category Name")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{

                val table = PartTable(machineName)
                transaction{
                    SchemaUtils.create(table)
                }

                val result = MachineRepo(categoryName).insertMachine(machineName,machineImage,machineDetails)
                call.respond(HttpStatusCode.OK,result!!)

            }catch (e: ExposedSQLException){
                call.respondText("duplicateName")
            }catch (e:Throwable){
                call.respondText(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")


    }

}