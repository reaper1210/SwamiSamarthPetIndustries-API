package com.swamisamarthpet.routes.downloadImagesRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.CategoryRepo
import com.swamisamarthpet.data.repository.MachineRepo
import com.swamisamarthpet.data.repository.PartRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Route.downloadAllPartImages(){

    get("$API_VERSION/downloadAllPartImages"){

//        val path = "./build/resources/main/static/images/"
//        val categoryList = CategoryRepo().getAllCategories()
//        val partImages = HashMap<String,HashMap<String,HashMap<String,ByteArray>>>()
//        for(category in categoryList){
//            val machineList = MachineRepo(category.categoryName).getAllMachines()
//            for(machine in machineList){
//                val partList = PartRepo(machine.machineName).getAllParts()
//                val currentMachineParts = HashMap<String,HashMap<String,ByteArray>>()
//                for(part in partList){
//                    currentMachineParts[part.partName] = part.partImages
//                }
//                partImages[machine.machineName] = currentMachineParts
//            }
//        }
//        call.respond(HttpStatusCode.OK,partImages)

    }

}