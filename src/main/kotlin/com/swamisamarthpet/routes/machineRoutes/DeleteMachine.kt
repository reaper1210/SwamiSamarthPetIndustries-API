package com.swamisamarthpet.routes.machineRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.repository.MachineRepo
import com.swamisamarthpet.data.tables.MachineTable
import com.swamisamarthpet.data.tables.PartTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.deleteMachine(){

    post("$API_VERSION/deleteMachine"){
        val parameters = call.receive<Parameters>()
        val machineId = parameters["machineId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Category Name")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                DatabaseFactory.dbQuery {
                    val machineTable = MachineTable(categoryName)
                    val machineName = machineTable
                        .select{machineTable.machineId eq machineId.toInt()}
                        .single()[machineTable.machineName]
                    val partTable = PartTable(machineName)
                    transaction{
                        SchemaUtils.drop(partTable)
                    }
                }
                val result = MachineRepo(categoryName).deleteMachine(machineId.toInt())
                call.respond(HttpStatusCode.OK,result)

            }catch (e: Throwable){
                call.respond(e.message.toString())
            }
        }
        else call.respondText("Invalid password")

    }

}