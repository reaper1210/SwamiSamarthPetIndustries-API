package com.swamisamarthpet.routes.ratingRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.RatingRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getRatings(){
    get("$API_VERSION/getRatings") {
        try{
            val result = RatingRepo().getRatings()
            call.respond(HttpStatusCode.OK,result)
        }catch (e: Throwable){
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }
    }
}