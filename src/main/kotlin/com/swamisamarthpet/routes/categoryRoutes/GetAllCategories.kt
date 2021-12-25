package com.swamisamarthpet.routes.categoryRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllCategories(){

    get("$API_VERSION/getAllCategories"){
        try{
            val result = CategoryRepo().getAllCategories()
            call.respond(HttpStatusCode.OK, result)
        }catch(e: Throwable){
            call.respond(HttpStatusCode.InternalServerError,e.message.toString())
        }

    }

}