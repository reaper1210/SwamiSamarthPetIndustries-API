package com.swamisamarthpet.routes.productRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import com.swamisamarthpet.data.repository.PopularRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.addPopularProduct(){
    post("$API_VERSION/insertPopularProduct"){

        val parameters = call.receiveParameters()
        val multiPartData = call.receiveMultipart()
        val productName = parameters["productName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing name")
        val productDetails = parameters["productDetails"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Details")
        val productType = parameters["productType"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Type")
        val productPopularity = parameters["productPopularity"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Popularity")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){

            try{
                val result = PopularRepo().insertPopularProduct(productName,multiPartData,productDetails,productType,productPopularity.toInt())
                call.respond(HttpStatusCode.OK,result)
            } catch (e:Throwable){
                call.respondText(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")
    }
}