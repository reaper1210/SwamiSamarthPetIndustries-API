package com.swamisamarthpet.data.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object RatingsTable:Table() {
    val ratingId: Column<Int> = integer("ratingId").autoIncrement()
    val fiveStars: Column<Int> = integer("fiveStars")
    val fourStars: Column<Int> = integer("fourStars")
    val threeStars: Column<Int> = integer("threeStars")
    val twoStars: Column<Int> = integer("twoStars")
    val oneStars: Column<Int> = integer("oneStars")

}