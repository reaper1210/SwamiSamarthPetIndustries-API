package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Machine(
    val machineId: Int,
    val machineName: String,
    val machineImages: String,
    val machineDetails: String,
    val machinePdf: String,
    val machinePopularity: Int
)
