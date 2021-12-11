package com.swamisamarthpet.routes.bannerRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.BannerRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.insertBanner(){
    post("$API_VERSION/insertBanner"){
        val parameters = call.receiveParameters()
        val multiPartData = call.receiveMultipart()
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing password")
        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                val result = BannerRepo().insertBanner(multiPartData)
                call.respond(HttpStatusCode.OK,result)
            }catch (e: Throwable){
                call.respond(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")

    }
}