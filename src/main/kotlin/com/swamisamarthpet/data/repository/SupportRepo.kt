package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.SupportDao
import com.swamisamarthpet.data.model.User
import com.swamisamarthpet.data.tables.RegisteredUsersTable
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.postgresql.util.PSQLException
import java.util.*

class SupportRepo: SupportDao {

    override suspend fun createUser(userName: String, phoneNumber: String): String = try{
        val userId = UUID.randomUUID().toString()
        DatabaseFactory.dbQuery {
            RegisteredUsersTable.insert {user->
                user[RegisteredUsersTable.userId] = userId
                user[RegisteredUsersTable.userName] = userName
                user[RegisteredUsersTable.phoneNumber] = phoneNumber
            }
        }
        userId
    }catch (e: Exception){
        getUserByPhoneNumber(phoneNumber)
    }

    override suspend fun getAllUsers(): List<User> = DatabaseFactory.dbQuery {
        RegisteredUsersTable.selectAll().mapNotNull {
            rowToCategoryUser(it)
        }
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): String = DatabaseFactory.dbQuery {
        val result = RegisteredUsersTable.select{ RegisteredUsersTable.phoneNumber eq phoneNumber }.mapNotNull {
            rowToCategoryUser(it)
        }
        result[0].userId
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

}