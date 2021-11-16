package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.SupportDao
import com.swamisamarthpet.data.model.User
import com.swamisamarthpet.data.tables.AllCategoriesTable
import com.swamisamarthpet.data.tables.PartTable
import com.swamisamarthpet.data.tables.RegisteredUsersTable
import com.swamisamarthpet.data.tables.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
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
        val table = UserTable(userId)
        transaction{
            SchemaUtils.create(table)
        }
        return User(userId,userName,phoneNumber)
    }
}