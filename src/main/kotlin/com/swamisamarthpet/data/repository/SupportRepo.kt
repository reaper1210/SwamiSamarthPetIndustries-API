package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.SupportDao
import com.swamisamarthpet.data.model.Message
import com.swamisamarthpet.data.model.User
import com.swamisamarthpet.data.tables.RegisteredUsersTable
import com.swamisamarthpet.data.tables.UserMessagesTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SupportRepo(): SupportDao {

    override suspend fun createUser(userName: String, phoneNumber: String): User {
        val userId = UUID.randomUUID().toString()
        DatabaseFactory.dbQuery {
            RegisteredUsersTable.insert {user->
                user[RegisteredUsersTable.userId] = userId
                user[RegisteredUsersTable.userName] = userName
                user[RegisteredUsersTable.phoneNumber] = phoneNumber
            }
        }
        val table = UserMessagesTable(userId)
        transaction{
            SchemaUtils.create(table)
        }
        return User(userId,userName,phoneNumber)
    }

    override suspend fun getAllUsers(): List<User> = DatabaseFactory.dbQuery {
        RegisteredUsersTable.selectAll().mapNotNull {
            rowToCategoryUser(it)
        }
    }

    override suspend fun sendMessage(userId: String, message: String, dateAndTime: String, messageFrom: String): Int {
        val userMessagesTable = UserMessagesTable(userId)
        var messageId = 0
        DatabaseFactory.dbQuery {
            userMessagesTable.insert { user ->
                user[userMessagesTable.message] = message
                user[userMessagesTable.dateAndTime] = dateAndTime
                user[userMessagesTable.messageFrom] = messageFrom
            }
        }
        DatabaseFactory.dbQuery {
            userMessagesTable.selectAll().mapNotNull {
                messageId = it[userMessagesTable.messageId]
            }
        }
        return messageId
    }

    override suspend fun getAllMessages(userId: String): List<Message> {
        val userMessagesTable = UserMessagesTable(userId)
        val result = DatabaseFactory.dbQuery {
            userMessagesTable.selectAll().mapNotNull {
                rowToCategoryMessage(it,userId)
            }
        }
        return result
    }

    override suspend fun sendMsg(msg: String) {

    }

    private fun rowToCategoryUser(row: ResultRow?): User? {
        if(row == null)
            return null

        return User(
            userId = row[RegisteredUsersTable.userId],
            userName = row[RegisteredUsersTable.userName],
            phoneNumber = row[RegisteredUsersTable.phoneNumber]
        )
    }

    private fun rowToCategoryMessage(row: ResultRow?, userId:String): Message? {
        val table = UserMessagesTable(userId)
        if(row == null)
            return null

        return Message(
            messageId = row[table.messageId],
            message = row[table.message],
            dateAndTime = row[table.dateAndTime],
            messageFrom = row[table.messageFrom]
        )
    }

}