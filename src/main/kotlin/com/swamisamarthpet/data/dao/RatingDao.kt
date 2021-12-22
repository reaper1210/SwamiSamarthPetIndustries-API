package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Rating

interface RatingDao {

    suspend fun insertRatings(
        rating:Int
    ):Int

    suspend fun getRatings(): List<Rating>
}