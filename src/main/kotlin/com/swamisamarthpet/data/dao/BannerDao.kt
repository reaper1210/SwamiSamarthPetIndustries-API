package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Banner
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

    suspend fun getAllBanners(): List<Banner>

}