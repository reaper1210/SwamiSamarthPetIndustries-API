package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.SupportDao
import com.swamisamarthpet.data.model.User
import com.swamisamarthpet.data.tables.RegisteredUsersTable
import org.jetbrains.exposed.sql.*
import java.util.*

class SupportRepo: SupportDao {

    override suspend fun createUser(userName: String, phoneNumber: String, token: String): String = try{
        val userId = UUID.randomUUID().toString()
        DatabaseFactory.dbQuery {
            RegisteredUsersTable.insert {user->
                user[RegisteredUsersTable.userId] = userId
                user[RegisteredUsersTable.userName] = userName
                user[RegisteredUsersTable.phoneNumber] = phoneNumber
                user[RegisteredUsersTable.lastMessageTime] = "0"
                user[RegisteredUsersTable.token] = token
            }
        }
        userId
    }catch (e: Exception){
        DatabaseFactory.dbQuery {
            RegisteredUsersTable.update({
                RegisteredUsersTable.phoneNumber eq phoneNumber
            }){ statement ->
                statement[RegisteredUsersTable.token] = token
            }
        }
        getUserByPhoneNumber(phoneNumber)
    }

    override suspend fun getAllUsers(): List<User> {
        return DatabaseFactory.dbQuery {
            RegisteredUsersTable.selectAll().orderBy(RegisteredUsersTable.lastMessageTime,SortOrder.DESC).mapNotNull {
                rowToCategoryUser(it)
            }
        }
    }

    override suspend fun updateUserLastMessageTime(userId: String, time: String): Int {
        return try{
            DatabaseFactory.dbQuery {
                RegisteredUsersTable.update({
                    RegisteredUsersTable.userId eq userId
                }){statement->
                    statement[lastMessageTime] = time.toString()
                }
            }
            1
        }catch (e:Throwable){
            0
        }
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): String {
        return DatabaseFactory.dbQuery {
            val result = RegisteredUsersTable.select { RegisteredUsersTable.phoneNumber eq phoneNumber }.mapNotNull {
                rowToCategoryUser(it)
            }
            result[0].userId
        }
    }

    private fun rowToCategoryUser(row: ResultRow?): User? {
        if(row == null)
            return null

        return User(
            userId = row[RegisteredUsersTable.userId],
            userName = row[RegisteredUsersTable.userName],
            phoneNumber = row[RegisteredUsersTable.phoneNumber],
            lastMessageTime = row[RegisteredUsersTable.lastMessageTime],
            token = row[RegisteredUsersTable.token]
        )
    }

}