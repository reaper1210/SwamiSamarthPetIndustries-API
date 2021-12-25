package com.swamisamarthpet.routes.productRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.PopularRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.deletePopularProduct(){
    post("$API_VERSION/deletePopularProduct"){
        val parameters = call.receiveParameters()
        val productId = parameters["productId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Product Id")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){

            try{
                val result = PopularRepo().deletePopularProduct(productId.toInt())
                call.respond(HttpStatusCode.OK,result)
            } catch (e:Throwable){
                call.respond(HttpStatusCode.InternalServerError, e.message.toString())
            }
        }
        else call.respond(HttpStatusCode.Unauthorized,"Invalid Password")
    }
}