package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object RegisteredUsersTable:Table() {
    val userId: Column<String> = varchar("userId",10485760)
    val userName: Column<String> = varchar("userName",500)
    val phoneNumber: Column<String> = varchar("phoneNumber",13).uniqueIndex()
    val lastMessageTime: Column<String> = varchar("lastMessageTime",10485760)
    val token: Column<String> = varchar("token",10485760)

    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}