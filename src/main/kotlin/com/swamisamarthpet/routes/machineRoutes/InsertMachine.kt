package com.swamisamarthpet.routes.machineRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.data.repository.MachineRepo
import com.swamisamarthpet.data.tables.PartTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionScope
import java.io.File

fun Route.insertMachine(){

    post("$API_VERSION/insertMachine"){

        val parameters = call.receiveParameters()
        val multiPart = call.receiveMultipart()
        val machineName = parameters["machineName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Name")
        val machineDetails = parameters["machineDetails"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Details")
        val machinePdf = parameters["machinePdf"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Pdf")
        val categoryName = parameters["categoryName"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Category Name")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{

                var i = 1
                val array = arrayListOf<String>()
                val imagePartArray = arrayListOf<PartData>()
                multiPart.forEachPart {
                    if(it is PartData.FileItem) {
                        array.add(machineName+i)
                        imagePartArray.add(it)
                        i++
                    }
                }
                val machineImages = array.joinToString(",")

                val result = MachineRepo(categoryName).insertMachine(machineName,machineImages,machineDetails,machinePdf)
                val table = PartTable(machineName)
                transaction{
                    SchemaUtils.create(table)
                }

                i = 0
                for(part in imagePartArray){
                    if(part is PartData.FileItem) {
                        val name = array[i]
                        val file = File("./build/resources/main/static/images/$name.png")
                        part.streamProvider().use { its ->
                            file.outputStream().buffered().use {
                                its.copyTo(it)
                            }
                        }
                    }
                    i++
                    part.dispose()
                }
                call.respond(HttpStatusCode.OK,result!!)

            }catch (e: ExposedSQLException){
                call.respondText("duplicateName")
            }catch (e:Throwable){
                call.respondText(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")

    }

}