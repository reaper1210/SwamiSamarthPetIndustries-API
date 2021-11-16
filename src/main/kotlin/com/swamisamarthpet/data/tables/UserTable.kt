package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

class UserTable(override val tableName: String):Table() {
    val message: Column<String> = varchar("message",10485760)
    val dateAndTime: Column<String> = varchar("dateAndTime",20)
    val messageFrom:Column<String> = varchar("messageFrom",20)
}