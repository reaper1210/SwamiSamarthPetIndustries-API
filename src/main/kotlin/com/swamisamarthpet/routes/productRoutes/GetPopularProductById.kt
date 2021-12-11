package com.swamisamarthpet.routes.productRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.PopularRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getPopularProductById(){
    post("/$API_VERSION/getPopularProductById"){
        val parameters = call.receiveParameters()
        val productId = parameters["productId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Product Id")

        try{
            val result = PopularRepo().getPopularProductById(productId.toInt())
            call.respond(HttpStatusCode.OK,result)
        }
        catch(e: Throwable){
            call.respond(e.message.toString())
        }
    }
}