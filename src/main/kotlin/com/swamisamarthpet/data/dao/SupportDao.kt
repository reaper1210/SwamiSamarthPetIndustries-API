package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Message
import com.swamisamarthpet.data.model.User
import java.rmi.server.UID

interface SupportDao {

    suspend fun createUser(
        userName:String,
        phoneNumber:String
    ): User

    suspend fun getAllUsers() : List<User>

    suspend fun sendMessage(
        userId:String,
        message:String,
        dateAndTime:String,
        messageFrom:String
    ): Message

    suspend fun getAllMessages(
        userId:String
    ):List<Message>

}