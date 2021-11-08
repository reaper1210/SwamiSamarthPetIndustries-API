package com.swamisamarthpet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Machine(
    val machineId: Int,
    val machineName: String,
    val machineImages: HashMap<Int, ByteArray>,
    val machineDetails: String,
    val machinePdf: String
)
