package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Machine

interface MachineDao {

    suspend fun insertMachine(
        machineName: String,
        machineImage: String,
        machineDetails: String,
        machinePdf: String
    ): Machine?

    suspend fun deleteMachine(
        machineId: Int
    ): Int

    suspend fun updateMachine(
        machineId: Int,
        machineImage: String,
        machineDetails: String,
        machinePdf: String
    ): Int

    suspend fun getMachineById(
        machineId: Int
    ): Machine?

    suspend fun getAllMachines(): List<Machine>

}