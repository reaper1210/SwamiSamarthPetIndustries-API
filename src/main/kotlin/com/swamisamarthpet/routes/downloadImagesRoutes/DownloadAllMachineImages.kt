package com.swamisamarthpet.routes.downloadImagesRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import com.swamisamarthpet.data.repository.MachineRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Route.downloadAllMachineImages(){

    get("$API_VERSION/downloadAllMachineImages"){

        val path = "./build/resources/main/static/images/"
        val categoryList = CategoryRepo().getAllCategories()
        val machineImages = HashMap<String,HashMap<String,ByteArray>>()
        for(category in categoryList){
            val machineList = MachineRepo(category.categoryName).getAllMachines()
            for(i in machineList.indices){
                val imageNameList = machineList[i].machineImage.split(",")
                val currentMachineImages = HashMap<String,ByteArray>()
                for(image in imageNameList){
                    val imageFile = File("$path$image.png")
                    currentMachineImages[image]=imageFile.readBytes()
                }
                machineImages[machineList[i].machineName] = currentMachineImages
            }
        }
        call.respond(HttpStatusCode.OK,machineImages)

    }

}