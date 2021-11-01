package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

class PartTable(override val tableName: String): Table() {

    val partId: Column<Int> = integer("partId").autoIncrement()
    val partName: Column<String> = varchar("partName",500).uniqueIndex()
    val partImages: Column<String> = varchar("partImage",5000)
    val partDetails: Column<String> = varchar("partDetails",5000)

    override val primaryKey: PrimaryKey =  PrimaryKey(partId)

}