package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Part

interface PartDao {

    suspend fun insertPart(
        partName: String,
        partImage: String,
        partDetails: String
    ): Part?

    suspend fun deletePart(
        partId: Int
    ): Int

    suspend fun updatePart(
        partId: Int,
        partImage: String,
        partDetails: String
    ): Int

    suspend fun getPartById(
        partId: Int
    ): Part?

    suspend fun getAllParts(): List<Part>

}