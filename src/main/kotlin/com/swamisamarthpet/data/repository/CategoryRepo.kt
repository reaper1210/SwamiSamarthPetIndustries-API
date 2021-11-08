package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.CategoryDao
import com.swamisamarthpet.data.model.Category
import com.swamisamarthpet.data.tables.AllCategoriesTable
import com.swamisamarthpet.data.tables.MachineTable
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.util.*

class CategoryRepo: CategoryDao {

    override suspend fun insertCategory(categoryName: String, multiPartData: MultiPartData): Int =
        try{
            multiPartData.forEachPart { part->
                if(part is PartData.FileItem) {
                    val file = File("./build/resources/main/static/$categoryName.png")
                    part.streamProvider().use { its ->
                        file.outputStream().buffered().use {
                            its.copyTo(it)
                        }
                        val bytes = Base64.getEncoder().encodeToString(file.readBytes())
                        DatabaseFactory.dbQuery {
                            AllCategoriesTable.insert { category ->
                                category[AllCategoriesTable.categoryName] = categoryName
                                category[AllCategoriesTable.categoryImage] = bytes
                            }
                        }
                        println(file.readBytes().toString())
                        return@forEachPart
                    }
                }
            }

            val table = MachineTable(categoryName)
            transaction {
                SchemaUtils.create(table)
            }
            1
        }catch (e: Throwable){
            e.printStackTrace()
            0
        }

    override suspend fun deleteCategory(categoryId: Int): Int =
        DatabaseFactory.dbQuery {
            AllCategoriesTable.deleteWhere { AllCategoriesTable.categoryId.eq(categoryId) }
        }

    override suspend fun updateCategory(categoryId: Int, categoryImage: PartData): Int =
        try{
            val categoryName = getCategoryById(categoryId)?.categoryName
            if(categoryImage is PartData.FileItem) {
                val file = File("./build/resources/main/static/$categoryName.png")
                categoryImage.streamProvider().use { its ->
                    file.outputStream().buffered().use {
                        its.copyTo(it)
                    }
                    val bytes = Base64.getEncoder().encodeToString(file.readBytes())
                    DatabaseFactory.dbQuery {
                        AllCategoriesTable.update({
                            AllCategoriesTable.categoryId.eq(categoryId)
                        }){ statement ->
                            statement[AllCategoriesTable.categoryImage] = bytes
                        }
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

        val image = Base64.getDecoder().decode(row[AllCategoriesTable.categoryImage])

        return Category(
            categoryId = row[AllCategoriesTable.categoryId],
            categoryName = row[AllCategoriesTable.categoryName],
            categoryImage = image
        )
    }

}