package com.swamisamarthpet.routes

import com.swamisamarthpet.API_VERSION
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Route.uploadImage(){

    val API_URL = System.getenv("API_URL")

    post("$API_VERSION/uploadImage"){
        try{
            val multipart = call.receiveMultipart()
            var imageName = ""
            multipart.forEachPart { part ->
                if(part is PartData.FileItem) {
                    val name = part.originalFileName!!
                    imageName = name
                    val file = File("./build/resources/main/static/$name")

                    part.streamProvider().use { its ->
                        file.outputStream().buffered().use {
                            its.copyTo(it)
                        }
                    }
                }
                part.dispose()
            }
            call.respond(HttpStatusCode.OK,"$API_URL/uploads/$imageName")
        }catch(e: Throwable){
            e.printStackTrace()
            call.respondText(e.message.toString())
        }

    }

}