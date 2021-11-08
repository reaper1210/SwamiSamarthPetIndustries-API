package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object AllCategoriesTable: Table() {

    override val tableName: String = "AllCategories"

    val categoryId: Column<Int> = integer("categoryId").autoIncrement()
    val categoryName: Column<String> = varchar("categoryName",500).uniqueIndex()
    val categoryImage: Column<String> = varchar("categoryImage",10485760)

    override val primaryKey: PrimaryKey = PrimaryKey(categoryId)

}