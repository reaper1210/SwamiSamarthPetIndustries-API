package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val oneStars:Int,
    val twoStars:Int,
    val threeStars:Int,
    val fourStars:Int,
    val fiveStars:Int
)
