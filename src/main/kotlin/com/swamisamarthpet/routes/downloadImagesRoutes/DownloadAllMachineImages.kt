package com.swamisamarthpet.routes.downloadImagesRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.repository.CategoryRepo
import com.swamisamarthpet.data.repository.MachineRepo
import com.swamisamarthpet.data.tables.MachineTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import java.io.File

fun Route.downloadAllMachineImages(){

    get("$API_VERSION/downloadAllMachineImages") {

        try {

            val categoryList = CategoryRepo().getAllCategories()
            val machineImages = HashMap<String,HashMap<String, String>>()//<categoryName,<machineName,imagesCommaSeparated>>
            for (category in categoryList) {
                val machineTable = MachineTable(category.categoryName)
                val machineList = DatabaseFactory.dbQuery {
                    machineTable.selectAll().mapNotNull {
                        MachineRepo(machineTable.tableName).rowToMachine(it)
                    }
                }
                val currentCategoryMachines = HashMap<String,String>()
                for (machine in machineList) {
                    currentCategoryMachines[machine.machineName] = machine.machineImages
                }
                machineImages[category.categoryName] = currentCategoryMachines
            }
            call.respond(HttpStatusCode.OK, machineImages)
        }catch(e:Exception) {
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }

    }

}