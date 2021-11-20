package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object RegisteredUsersTable:Table() {
    val userId: Column<String> = varchar("userId",10485760)
    val userName: Column<String> = varchar("userName",500)
    val phoneNumber: Column<String> = varchar("phoneNumber",13).uniqueIndex()

    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}