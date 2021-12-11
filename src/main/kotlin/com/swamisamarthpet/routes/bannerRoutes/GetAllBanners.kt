package com.swamisamarthpet.routes.bannerRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.BannerRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllBanners(){
    get("$API_VERSION/getAllBanners"){
        try{
            val result = BannerRepo().getAllBanners()
            call.respond(HttpStatusCode.OK,result)
        }catch (e: Throwable){
            call.respond(e.message.toString())
        }
    }
}