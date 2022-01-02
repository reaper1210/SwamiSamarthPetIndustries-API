package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val userName: String,
    val phoneNumber: String,
    val unreadMessages: String,
    val lastMessageTime: String
)