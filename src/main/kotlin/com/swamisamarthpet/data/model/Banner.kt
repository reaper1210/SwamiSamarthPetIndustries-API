package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    val bannerId: Int,
    val bannerImage: String
)