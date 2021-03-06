package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.User

interface SupportDao {

    suspend fun createUser(
        userName:String,
        phoneNumber:String,
        token:String
    ): String

    suspend fun getUserByPhoneNumber(
        phoneNumber: String
    ): String

    suspend fun getAllUsers() : List<User>
    suspend fun updateUserLastMessageTime(userId: String, time: String): Int
}