package com.swamisamarthpet.routes.productRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.PopularRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllPopularProducts(){

    get("$API_VERSION/getAllPopularProducts"){
        try{
            val result = PopularRepo().getAllPopularProducts()
            call.respond(HttpStatusCode.OK,result)
        }catch (e: Throwable){
            call.respond(e.message.toString())
        }


    }

}