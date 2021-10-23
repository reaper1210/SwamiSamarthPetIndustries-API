package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.CategoryDao
import com.swamisamarthpet.data.model.Category
import com.swamisamarthpet.data.tables.AllCategoriesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class CategoryRepo: CategoryDao {

    override suspend fun insertCategory(categoryName: String, categoryImage: String): Category? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = AllCategoriesTable.insert { category ->
                category[AllCategoriesTable.categoryName] = categoryName
                category[AllCategoriesTable.categoryImage] = categoryImage
            }
        }
        return rowToCategory(statement?.resultedValues?.get(0))
    }

    override suspend fun deleteCategory(categoryId: Int): Int =
        DatabaseFactory.dbQuery {
            AllCategoriesTable.deleteWhere { AllCategoriesTable.categoryId.eq(categoryId) }
        }

    override suspend fun updateCategory(categoryId: Int, categoryImage: String): Int =
        DatabaseFactory.dbQuery {
            AllCategoriesTable.update({
                AllCategoriesTable.categoryId.eq(categoryId)
            }){ statement->
                statement[AllCategoriesTable.categoryImage] = categoryImage
            }
        }

    override suspend fun getAllCategories(): List<Category> = DatabaseFactory.dbQuery {
        AllCategoriesTable.selectAll().mapNotNull {
            rowToCategory(it)
        }
    }

    override suspend fun getCategoryById(categoryId: Int): Category? =
        DatabaseFactory.dbQuery {
        AllCategoriesTable.select {
            AllCategoriesTable.categoryId.eq(categoryId)
        }.map {
            rowToCategory(it)
        }.singleOrNull()
    }

    private fun rowToCategory(row: ResultRow?): Category? {
        if(row == null)
            return null
        return Category(
            categoryId = row[AllCategoriesTable.categoryId],
            categoryName = row[AllCategoriesTable.categoryName],
            categoryImage = row[AllCategoriesTable.categoryImage]
        )
    }

}