package com.swamisamarthpet.plugins

import com.swamisamarthpet.routes.bannerRoutes.deleteBanner
import com.swamisamarthpet.routes.bannerRoutes.getAllBanners
import com.swamisamarthpet.routes.bannerRoutes.insertBanner
import com.swamisamarthpet.routes.bannerRoutes.updateBanner
import com.swamisamarthpet.routes.categoryRoutes.deleteCategory
import com.swamisamarthpet.routes.categoryRoutes.getAllCategories
import com.swamisamarthpet.routes.categoryRoutes.getCategoryById
import com.swamisamarthpet.routes.categoryRoutes.updateCategory
import com.swamisamarthpet.routes.categoryRoutes.insertCategory
import com.swamisamarthpet.routes.downloadImagesRoutes.downloadAllCategoryImages
import com.swamisamarthpet.routes.downloadImagesRoutes.downloadAllMachineImages
import com.swamisamarthpet.routes.downloadImagesRoutes.downloadAllPartImages
import com.swamisamarthpet.routes.machineRoutes.*
import com.swamisamarthpet.routes.partRoutes.*
import com.swamisamarthpet.routes.productRoutes.*
import com.swamisamarthpet.routes.ratingRoutes.getRatings
import com.swamisamarthpet.routes.ratingRoutes.insertRating
import com.swamisamarthpet.routes.supportRoutes.*
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

        downloadAllCategoryImages()
        downloadAllMachineImages()
        downloadAllPartImages()

        createUser()
        getAllUsers()
        updateLastMessageTime()

        updatePopularProduct()
        insertPopularProduct()
        deletePopularProduct()
        getAllPopularProducts()
        getPopularProductById()

        insertBanner()
        updateBanner()
        deleteBanner()
        getAllBanners()

        insertRating()
        getRatings()

        static {
            resources("static")
        }

    }

}
