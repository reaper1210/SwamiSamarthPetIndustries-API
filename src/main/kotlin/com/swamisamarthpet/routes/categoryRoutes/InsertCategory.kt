package com.swamisamarthpet.routes.categoryRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import com.swamisamarthpet.data.tables.MachineTable
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun Route.insertCategory() {
    post("$API_VERSION/insertCategory"){

        val parameters = call.receiveParameters()
        val multiPartData = call.receiveMultipart()
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing name")
        val categoryImage = multiPartData.readPart()?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Image")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){

            try{
                val result = CategoryRepo().insertCategory(categoryName,categoryImage)
                categoryImage.dispose()
                call.respond(HttpStatusCode.OK,result)
            } catch (e:Throwable){
                call.respondText(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")

    }
}

