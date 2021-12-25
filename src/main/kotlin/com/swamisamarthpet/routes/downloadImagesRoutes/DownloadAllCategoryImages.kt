package com.swamisamarthpet.routes.downloadImagesRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Route.downloadAllCategoryImages(){

    get("$API_VERSION/downloadAllCategoryImages"){

        try{
            val categoryList = CategoryRepo().getAllCategories()
            val categoryImages = HashMap<String,String>()
            for(category in categoryList){
                categoryImages[category.categoryName] = category.categoryImage
            }
            call.respond(HttpStatusCode.OK,categoryImages)
        }catch(e: Exception){
            call.respond(HttpStatusCode.InternalServerError,e.message.toString())
        }

    }

}