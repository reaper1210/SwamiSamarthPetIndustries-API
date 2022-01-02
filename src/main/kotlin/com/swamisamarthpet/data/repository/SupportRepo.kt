package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.SupportDao
import com.swamisamarthpet.data.model.User
import com.swamisamarthpet.data.tables.RegisteredUsersTable
import org.jetbrains.exposed.sql.*
import java.util.*

class SupportRepo: SupportDao {

    override suspend fun createUser(userName: String, phoneNumber: String): String {
        val userId = UUID.randomUUID().toString()
        DatabaseFactory.dbQuery {
            RegisteredUsersTable.insert {user->
                user[RegisteredUsersTable.userId] = userId
                user[RegisteredUsersTable.userName] = userName
                user[RegisteredUsersTable.phoneNumber] = phoneNumber
                user[RegisteredUsersTable.unreadMessages] = "0"
                user[RegisteredUsersTable.lastMessageTime] = "0"
            }
        }
        return userId
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

    override suspend fun updateUserUnread(userId: String): Int {
        return try{
            DatabaseFactory.dbQuery {
                val userInfo = RegisteredUsersTable.select{RegisteredUsersTable.userId eq userId}.mapNotNull {
                    rowToCategoryUser(it)
                }
                RegisteredUsersTable.update({
                    RegisteredUsersTable.userId eq userId
                }){statement->
                    statement[unreadMessages] = (userInfo[0].unreadMessages.toInt()+1).toString()
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
            unreadMessages = row[RegisteredUsersTable.unreadMessages],
            lastMessageTime = row[RegisteredUsersTable.lastMessageTime]
        )
    }

}