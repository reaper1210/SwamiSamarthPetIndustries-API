package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.User

interface SupportDao {

    suspend fun createUser(
        userName:String,
        phoneNumber:String
    ): User

    suspend fun getAllUsers() : List<User>

}