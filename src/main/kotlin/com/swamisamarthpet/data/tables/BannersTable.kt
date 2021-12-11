package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object BannersTable: Table() {

    val bannerId: Column<Int> = integer("bannerId").autoIncrement()
    val bannerImage: Column<String> = varchar("bannerImage",10485760)

    override val primaryKey: PrimaryKey = PrimaryKey(bannerId)

}