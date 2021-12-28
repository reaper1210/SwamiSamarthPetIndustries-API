package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PopularProductsTable: Table(){

    val productId: Column<Int> = integer("productId").autoIncrement()
    val productName: Column<String> = varchar("productName",500).uniqueIndex()
    val productImages: Column<String> = varchar("productImage",10485760)
    val productDetails: Column<String> = varchar("productDetails",5000)
    val productPdf: Column<String> = varchar("productPdf",10485760)
    val productType: Column<String> = varchar("productType",10)
    val productPopularity: Column<Int> = integer("productPopularity")
    val productYoutubeVideo: Column<String> = varchar("productYoutubeVideo",10485760)

    override val primaryKey: PrimaryKey = PrimaryKey(productId)
}