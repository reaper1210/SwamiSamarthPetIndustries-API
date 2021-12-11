package com.swamisamarthpet.routes.productRoutes

import com.swamisamarthpet.API_VERSION
import io.ktor.routing.*

fun Route.getPopularProductById(){
    post("/$API_VERSION/getPopularProductById"){
        
    }
}