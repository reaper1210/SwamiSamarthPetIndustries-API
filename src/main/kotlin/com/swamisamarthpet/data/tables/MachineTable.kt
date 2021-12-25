package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

class MachineTable(override val tableName: String): Table(){

    val machineId: Column<Int> = integer("machineId").autoIncrement()
    val machineName: Column<String> = varchar("machineName",500).uniqueIndex()
    val machineImages: Column<String> = varchar("machineImage",10485760)
    val machineDetails: Column<String> = varchar("machineDetails",5000)
    val machinePdf: Column<String> = varchar("machinePdf",10485760)
    val youtubeVideoLink: Column<String> = varchar("youtubeVideoLink",10485760)

    override val primaryKey: PrimaryKey = PrimaryKey(machineId)

}