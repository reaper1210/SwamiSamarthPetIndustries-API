package com.swamisamarthpet.routes.ratingRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.RatingRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.insertRating(){
    post("$API_VERSION/insertRating") {
        val parameters = call.receive<Parameters>()
        val rating = parameters["rating"]?.toInt() ?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing bannerId")
        if(rating < 6){
            try{
                val result = RatingRepo().insertRatings(rating)
                call.respond(HttpStatusCode.OK,result)
            }catch (e: Throwable){
                call.respond(e.message.toString())
            }
        }
    }
}