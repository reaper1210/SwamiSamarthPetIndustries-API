package com.swamisamarthpet.routes.categoryRoutes

import com.swamisamarthpet.API_VERSION
import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.repository.CategoryRepo
import com.swamisamarthpet.data.tables.AllCategoriesTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.absoluteValue

fun Route.deleteCategory(){

    post("$API_VERSION/deleteCategory"){
        val parameters = call.receive<Parameters>()
        val categoryId = parameters["categoryId"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Id")
        val adminPass = parameters["adminPassword"]?: return@post call.respond(HttpStatusCode.Unauthorized,"Missing Password")

        if(adminPass==System.getenv("ADMIN_PASSWORD")){
            try{

                DatabaseFactory.dbQuery {
                    val categoryName = AllCategoriesTable
                        .select { AllCategoriesTable.categoryId.eq(categoryId.toInt())}
                        .single()[AllCategoriesTable.categoryName]
                    val machineTable = Table(categoryName)
                    transaction {
                        SchemaUtils.drop(machineTable)
                    }
                }

                val result = CategoryRepo().deleteCategory(categoryId.toInt())
                call.respond(HttpStatusCode.OK,result)

            }catch(e: Throwable){
                call.respond(e.message.toString())
            }
        }
        else call.respondText("Invalid Password")

    }

}