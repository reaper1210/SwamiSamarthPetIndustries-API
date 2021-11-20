package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Member
import com.swamisamarthpet.data.model.Message
import com.swamisamarthpet.data.model.User
import io.ktor.http.cio.websocket.*
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
        messageFrom:String,
        member:Member
    ): Int

    suspend fun getAllMessages(
        userId:String
    ):List<Message>

    suspend fun onJoin(
        userId:String,
        sessionId:String,
        socket: WebSocketSession
    ):Member

}