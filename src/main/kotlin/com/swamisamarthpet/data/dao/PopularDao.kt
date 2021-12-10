package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Machine
import com.swamisamarthpet.data.model.PopularProduct
import io.ktor.http.content.*

interface PopularDao {

    suspend fun insertPopularProduct(
        productName: String,
        multiPart: MultiPartData,
        productDetails: String,
        productType: String,
        productPopularity: Int
    ): Int

    suspend fun deletePopularProduct(
        productId: Int
    ): Int

    suspend fun updatePopularProduct(
        productId: Int,
        multiPart: MultiPartData,
        productDetails: String,
        productPopularity: Int
    ): Int

    suspend fun getPopularProductById(
        productId: Int
    ): PopularProduct

    suspend fun getAllPopularProducts(): List<HashMap<String,String>>
}