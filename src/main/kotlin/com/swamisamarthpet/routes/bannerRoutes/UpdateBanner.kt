package com.swamisamarthpet.routes.bannerRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.BannerRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateBanner(){
    post("$API_VERSION/updateBanner"){
        val parameters = call.receiveParameters()
        val multiPartData = call.receiveMultipart()
        val bannerId = parameters["bannerId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing bannerId")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing password")
        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{
                val result = BannerRepo().updateBanner(bannerId.toInt(),multiPartData)
                call.respond(HttpStatusCode.OK,result)
            }catch (e: Throwable){
                call.respond(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")
    }
}