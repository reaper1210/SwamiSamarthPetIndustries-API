package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Part(
    val partId: Int,
    val partName: String,
    val partImages: HashMap<String,ByteArray>,
    val partDetails: String
)
