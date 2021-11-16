package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val messageId: Int,
    val message:String,
    val dateAndTime:String,
    val messageFrom:String
)