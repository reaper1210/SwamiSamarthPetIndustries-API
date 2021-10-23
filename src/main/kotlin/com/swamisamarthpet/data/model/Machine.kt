package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Machine(
    val machineId: Int,
    val machineName: String,
    val machineImage: String,
    val machineDetails: String
)
