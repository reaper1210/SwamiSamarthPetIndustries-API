package com.swamisamarthpet.routes.categoryRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getCategoryById(){

    post("$API_VERSION/getCategoryById"){

        val parameters = call.receive<Parameters>()
        val categoryId = parameters["categoryId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")

        try{

            val category = CategoryRepo().getCategoryById(categoryId.toInt())
            call.respond(HttpStatusCode.OK,category!!)

        }catch (e: Throwable){
            call.respond(HttpStatusCode.InternalServerError,e.message.toString())
        }

    }

}