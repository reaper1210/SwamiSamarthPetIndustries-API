package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.User

interface SupportDao {

    suspend fun createUser(
        userName:String,
        phoneNumber:String
    ): String

    suspend fun getUserByPhoneNumber(
        phoneNumber: String
    ): String

    suspend fun getAllUsers() : List<User>
}