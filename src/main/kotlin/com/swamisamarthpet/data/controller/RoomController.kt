package com.swamisamarthpet.data.controller

import com.swamisamarthpet.data.model.Member
import com.swamisamarthpet.data.model.Message
import com.swamisamarthpet.data.repository.SupportRepo
import io.ktor.http.cio.websocket.*

class RoomController(private val supportRepo:SupportRepo) {

    private var member: Member? = null
    fun onJoin(
        userId:String,
        sessionId:String,
        socket:WebSocketSession
    ){
        member = Member(userId,sessionId,socket)
    }

    suspend fun sendMessage(userName:String,message:String){
        member?.socket?.send(Frame.Text(message))
    }

}