package com.swamisamarthpet.data.dao

import io.ktor.http.content.*

interface BannerDao {

    suspend fun insertBanner(
        multiPartData: MultiPartData
    ): Int

    suspend fun updateBanner(
        bannerId: Int,
        multiPartData: MultiPartData
    ): Int

    suspend fun deleteBanner(
        bannerId: Int
    ): Int

    suspend fun getAllBanners(): List<String>

}