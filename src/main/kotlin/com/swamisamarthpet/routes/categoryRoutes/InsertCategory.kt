package com.swamisamarthpet.routes.categoryRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import com.swamisamarthpet.data.tables.MachineTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.insertCategory() {
    post("$API_VERSION/insertCategory"){

        val parameters = call.receive<Parameters>()
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing name")
        val categoryImage = parameters["categoryImage"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Image")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{

                val table = MachineTable(categoryName)
                transaction {
                    SchemaUtils.create(table)
                }

                val result = CategoryRepo().insertCategory(categoryName,categoryImage)
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

