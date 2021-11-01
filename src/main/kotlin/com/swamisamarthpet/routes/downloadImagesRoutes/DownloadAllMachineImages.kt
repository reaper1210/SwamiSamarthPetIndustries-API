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

        val categoryList = CategoryRepo().getAllCategories()
        val machineImages = HashMap<String,HashMap<String,ByteArray>>()
        for(category in categoryList){
            val machineList = MachineRepo(category.categoryName).getAllMachines()
            for(machine in machineList){
                machineImages[machine.machineName] = machine.machineImages
            }
        }
        call.respond(HttpStatusCode.OK,machineImages)

    }

}