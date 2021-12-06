package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Machine
import io.ktor.http.content.*

interface MachineDao {

    suspend fun insertMachine(
        machineName: String,
        multiPart: MultiPartData,
        machineDetails: String,
        machinePdf: String,
        machinePopularity: Int
    ): Int

    suspend fun deleteMachine(
        machineId: Int
    ): Int

    suspend fun updateMachine(
        machineId: Int,
        multiPart: MultiPartData,
        machineDetails: String,
        machinePdf: String,
        machinePopularity: Int
    ): Int

    suspend fun getMachineById(
        machineId: Int
    ): Machine?

    suspend fun getAllMachines(): List<HashMap<String,String>>

}