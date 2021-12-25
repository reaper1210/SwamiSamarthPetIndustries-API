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

    get("$API_VERSION/downloadAllMachineImages") {

        try {

            val categoryList = CategoryRepo().getAllCategories()
            val machineImages = HashMap<String, String>()
            for (category in categoryList) {
                val machineList = MachineRepo(category.categoryName).getAllMachines()
                for (machineInfo in machineList) {
                    val machine =
                        MachineRepo(category.categoryName).getMachineById(machineInfo["machineId"]?.toInt()!!)!!
                    machineImages[machine.machineName] = machine.machineImages
                }
            }
            call.respond(HttpStatusCode.OK, machineImages)
        }catch(e:Exception) {
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }

    }

}