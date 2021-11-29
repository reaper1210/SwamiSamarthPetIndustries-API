package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.SupportDao
import com.swamisamarthpet.data.model.User
import com.swamisamarthpet.data.tables.RegisteredUsersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class SupportRepo: SupportDao {

    override suspend fun createUser(userName: String, phoneNumber: String): User {
        val userId = UUID.randomUUID().toString()
        DatabaseFactory.dbQuery {
            RegisteredUsersTable.insert {user->
                user[RegisteredUsersTable.userId] = userId
                user[RegisteredUsersTable.userName] = userName
                user[RegisteredUsersTable.phoneNumber] = phoneNumber
            }
        }
        return User(userId,userName,phoneNumber)
    }

    override suspend fun getAllUsers(): List<User> = DatabaseFactory.dbQuery {
        RegisteredUsersTable.selectAll().mapNotNull {
            rowToCategoryUser(it)
        }
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