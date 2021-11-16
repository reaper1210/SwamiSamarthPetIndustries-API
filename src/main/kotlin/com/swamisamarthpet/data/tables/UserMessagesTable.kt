package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

class UserMessagesTable(override val tableName: String):Table() {
    val messageId: Column<Int> = integer("messageId").autoIncrement()
    val message: Column<String> = varchar("message",10485760)
    val dateAndTime: Column<String> = varchar("dateAndTime",40)
    val messageFrom:Column<String> = varchar("messageFrom",20)

    override val primaryKey: PrimaryKey = PrimaryKey(messageId)

}