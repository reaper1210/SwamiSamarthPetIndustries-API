package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.CategoryDao
import com.swamisamarthpet.data.model.Category
import com.swamisamarthpet.data.tables.AllCategoriesTable
import com.swamisamarthpet.data.tables.MachineTable
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class CategoryRepo: CategoryDao {

    override suspend fun insertCategory(categoryName: String, categoryImage: PartData): Int =
        try{
            var statement: InsertStatement<Number>? = null
            DatabaseFactory.dbQuery {
                statement = AllCategoriesTable.insert { category ->
                    category[AllCategoriesTable.categoryName] = categoryName
                    category[AllCategoriesTable.categoryImage] = categoryName
                }
            }
            if(categoryImage is PartData.FileItem) {
                val file = File("./build/resources/main/static/images/$categoryName.png")
                categoryImage.streamProvider().use { its ->
                    file.outputStream().buffered().use {
                        its.copyTo(it)
                    }
                }
            }
            val table = MachineTable(categoryName)
            transaction {
                SchemaUtils.create(table)
            }
            val categoryId = rowToCategory(statement?.resultedValues?.get(0))?.categoryId!!
            categoryId
        }catch (e: Throwable){
            -1
        }

    override suspend fun deleteCategory(categoryId: Int): Int =
        DatabaseFactory.dbQuery {
            AllCategoriesTable.deleteWhere { AllCategoriesTable.categoryId.eq(categoryId) }
        }

    override suspend fun updateCategory(categoryId: Int, categoryImage: PartData): Int =
        try{
            val categoryName = getCategoryById(categoryId)?.categoryName
            if(categoryImage is PartData.FileItem) {
                val file = File("./build/resources/main/static/images/$categoryName.png")
                categoryImage.streamProvider().use { its ->
                    file.outputStream().buffered().use {
                        its.copyTo(it)
                    }
                }
            }
            1
        }catch(e: Throwable){
            0
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

        val imageFile = File("./build/resources/main/static/images/${row[AllCategoriesTable.categoryImage]}.png")
        val image = imageFile.readBytes()

        return Category(
            categoryId = row[AllCategoriesTable.categoryId],
            categoryName = row[AllCategoriesTable.categoryName],
            categoryImage = image
        )
    }

}