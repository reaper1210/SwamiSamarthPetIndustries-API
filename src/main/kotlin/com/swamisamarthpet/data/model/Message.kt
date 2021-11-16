package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val message:String,
    val dateAndTime:String,
    val messageFrom:String
)