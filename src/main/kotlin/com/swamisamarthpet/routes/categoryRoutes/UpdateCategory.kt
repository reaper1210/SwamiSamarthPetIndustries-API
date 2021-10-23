package com.swamisamarthpet.routes.categoryRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateCategory(){

    post("$API_VERSION/updateCategory"){

        val parameters = call.receive<Parameters>()
        val categoryId = parameters["categoryId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")
        val categoryImage = parameters["categoryImage"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Image")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                val result = CategoryRepo().updateCategory(categoryId.toInt(),categoryImage)
                call.respond(HttpStatusCode.OK,result)
            } catch(e: Throwable){
                call.respondText(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")


    }

}