package com.swamisamarthpet.plugins

import io.ktor.application.*

fun configureDoubleReceive(app: Application){

    DoubleReceiveFixed.install(app){}

}