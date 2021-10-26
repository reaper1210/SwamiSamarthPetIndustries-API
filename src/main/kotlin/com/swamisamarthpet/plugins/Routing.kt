package com.swamisamarthpet.plugins

import com.swamisamarthpet.routes.categoryRoutes.deleteCategory
import com.swamisamarthpet.routes.categoryRoutes.getAllCategories
import com.swamisamarthpet.routes.categoryRoutes.getCategoryById
import com.swamisamarthpet.routes.categoryRoutes.updateCategory
import com.swamisamarthpet.routes.categoryRoutes.insertCategory
import com.swamisamarthpet.routes.machineRoutes.*
import com.swamisamarthpet.routes.partRoutes.*
import io.ktor.routing.*
import io.ktor.http.content.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {

    routing {

        get("/") {
                call.respondText("SSPI API")
        }

        insertCategory()
        deleteCategory()
        updateCategory()
        getCategoryById()
        getAllCategories()

        insertMachine()
        deleteMachine()
        updateMachine()
        getMachineById()
        getAllMachines()

        insertPart()
        deletePart()
        updatePart()
        getPartById()
        getAllParts()

        static("/static") {
            resources("static")
        }

    }

}
