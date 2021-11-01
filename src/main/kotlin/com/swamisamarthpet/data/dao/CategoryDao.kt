package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Category
import io.ktor.http.content.*

interface CategoryDao {

    suspend fun insertCategory(
        categoryName: String,
        multiPartData: MultiPartData
    ): Int

    suspend fun deleteCategory(
        categoryId: Int
    ): Int

    suspend fun updateCategory(
        categoryId: Int,
        categoryImage: PartData
    ): Int

    suspend fun getAllCategories(): List<Category>

    suspend fun getCategoryById(
        categoryId:Int
    ): Category?

}