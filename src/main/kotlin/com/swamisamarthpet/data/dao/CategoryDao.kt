package com.swamisamarthpet.data.dao

import com.swamisamarthpet.data.model.Category

interface CategoryDao {

    suspend fun insertCategory(
        categoryName: String,
        categoryImage: String
    ): Category?

    suspend fun deleteCategory(
        categoryId: Int
    ): Int

    suspend fun updateCategory(
        categoryId: Int,
        categoryImage: String
    ): Int

    suspend fun getAllCategories(): List<Category>

    suspend fun getCategoryById(
        categoryId:Int
    ): Category?

}