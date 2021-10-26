package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

class MachineTable(override val tableName: String): Table(){

    val machineId: Column<Int> = integer("machineId").autoIncrement()
    val machineName: Column<String> = varchar("machineName",500).uniqueIndex()
    val machineImage: Column<String> = varchar("machineImage",5000)
    val machineDetails: Column<String> = varchar("machineDetails",5000)
    val machinePdf: Column<String> = varchar("machinePdf",5000)

    override val primaryKey: PrimaryKey = PrimaryKey(machineId)

}