package com.swamisamarthpet.routes.downloadImagesRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.repository.CategoryRepo
import com.swamisamarthpet.data.repository.MachineRepo
import com.swamisamarthpet.data.repository.PartRepo
import com.swamisamarthpet.data.tables.MachineTable
import com.swamisamarthpet.data.tables.PartTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Except
import org.jetbrains.exposed.sql.selectAll
import java.lang.Exception

fun Route.downloadAllPartImages(){

    get("$API_VERSION/downloadAllPartImages"){

        try{
            val categoryList = CategoryRepo().getAllCategories()
            val partImages = HashMap<String,HashMap<String,HashMap<String,String>>>()//<categoryName,<machineName,<partName,imagesCommaSeparated>>>
            for(category in categoryList){
                val machineTable = MachineTable(category.categoryName)
                val machineList = DatabaseFactory.dbQuery{
                    machineTable.selectAll().mapNotNull {
                        MachineRepo(machineTable.tableName).rowToMachine(it)
                    }
                }
                val currentCategoryParts = HashMap<String,HashMap<String,String>>()
                for(machine in machineList){
                    val partTable = PartTable(machine.machineName)
                    val partList = DatabaseFactory.dbQuery {
                        partTable.selectAll().mapNotNull {
                            PartRepo(partTable.tableName).rowToPart(it)
                        }
                    }
                    val currentMachineParts = HashMap<String,String>()
                    for(part in partList){
                        currentMachineParts[part.partName] = part.partImages
                    }
                    currentCategoryParts[machine.machineName] = currentMachineParts
                }
                partImages[category.categoryName] = currentCategoryParts
            }
            call.respond(HttpStatusCode.OK,partImages)
        }catch (e: Exception){
            call.respond(HttpStatusCode.InternalServerError, e.message.toString())
        }


    }

}