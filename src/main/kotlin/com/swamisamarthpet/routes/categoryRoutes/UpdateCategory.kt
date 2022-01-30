package com.swamisamarthpet.routes.categoryRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateCategory(){

    post("$API_VERSION/updateCategory"){

        val parameters = call.receive<Parameters>()
        val multiPart = call.receiveMultipart()
        val categoryId = parameters["categoryId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")
        var categoryImage: PartData? = null

        multiPart.forEachPart { partData ->
            if(partData is PartData.FileItem) {
                categoryImage = partData
            }
        }
        if(categoryImage==null) {
            return@post call.respond(HttpStatusCode.Unauthorized, "Missing Id")
        }

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                val result = CategoryRepo().updateCategory(categoryId.toInt(),categoryImage!!)
                call.respond(HttpStatusCode.OK,result)
            } catch(e: Throwable){
                call.respond(HttpStatusCode.InternalServerError,e.message.toString())
            }
        }
        else call.respond(HttpStatusCode.Unauthorized,"Invalid Password")


    }

}