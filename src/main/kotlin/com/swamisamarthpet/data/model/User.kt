package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
class User(
    val userId: String,
    val userName: String,
    val phoneNumber: String
)