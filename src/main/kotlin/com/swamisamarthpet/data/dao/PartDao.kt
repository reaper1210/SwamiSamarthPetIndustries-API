package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Part
import io.ktor.http.content.*

interface PartDao {

    suspend fun insertPart(
        partName: String,
        multiPart: MultiPartData,
        partDetails: String,
        machineName: String
    ): Int

    suspend fun deletePart(
        partId: Int
    ): Int

    suspend fun updatePart(
        partId: Int,
        multiPart: MultiPartData,
        partDetails: String,
        machineName: String
    ): Int

    suspend fun getPartById(
        partId: Int
    ): Part?

    suspend fun getAllParts(): List<HashMap<String,String>>

}