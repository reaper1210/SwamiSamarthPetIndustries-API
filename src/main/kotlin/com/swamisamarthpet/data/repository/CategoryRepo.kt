package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.CategoryDao
import com.swamisamarthpet.data.model.Category
import com.swamisamarthpet.data.tables.AllCategoriesTable
import com.swamisamarthpet.data.tables.MachineTable
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import java.util.zip.Deflater

class CategoryRepo: CategoryDao {

    override suspend fun insertCategory(categoryName: String, multiPartData: MultiPartData): Int {
        multiPartData.forEachPart { part->
            if(part is PartData.FileItem) {
                val file = File("./build/resources/main/static/$categoryName.png")
                part.streamProvider().use { its ->
                    file.outputStream().buffered().use {
                        its.copyTo(it)
                    }

                    //adminAppCode
                    val compressor = Deflater()
                    compressor.setLevel(Deflater.BEST_COMPRESSION)
                    compressor.setInput(file.readBytes())
                    compressor.finish()
                    val bos = ByteArrayOutputStream(file.readBytes().size)
                    val buf = ByteArray(1024)
                    while (!compressor.finished()) {
                        val count = compressor.deflate(buf)
                        bos.write(buf, 0, count)
                    }
                    bos.close()
                    val byteArrayString = bos.toByteArray().contentToString()

                    DatabaseFactory.dbQuery {
                        AllCategoriesTable.insert { category ->
                            category[AllCategoriesTable.categoryName] = categoryName
                            category[categoryImage] = byteArrayString
                        }
                    }
                    return@forEachPart
                }
            }
        }
        val table = MachineTable(categoryName)
        transaction {
            SchemaUtils.create(table)
        }
        return 1
    }

    override suspend fun deleteCategory(categoryId: Int): Int {
        return DatabaseFactory.dbQuery {
            AllCategoriesTable.deleteWhere { AllCategoriesTable.categoryId.eq(categoryId) }
        }
    }

    override suspend fun updateCategory(categoryId: Int, categoryImage: PartData): Int {
        val categoryName = getCategoryById(categoryId)?.categoryName
        if(categoryImage is PartData.FileItem) {
            val file = File("./build/resources/main/static/$categoryName.png")
            categoryImage.streamProvider().use { its ->
                file.outputStream().buffered().use {
                    its.copyTo(it)
                }
                //adminAppCode
                val compressor = Deflater()
                compressor.setLevel(Deflater.BEST_COMPRESSION)
                compressor.setInput(file.readBytes())
                compressor.finish()
                val bos = ByteArrayOutputStream(file.readBytes().size)
                val buf = ByteArray(1024)
                while (!compressor.finished()) {
                    val count = compressor.deflate(buf)
                    bos.write(buf, 0, count)
                }
                bos.close()
                val byteArrayString = bos.toByteArray().contentToString()
                DatabaseFactory.dbQuery {
                    AllCategoriesTable.update({
                        AllCategoriesTable.categoryId.eq(categoryId)
                    }){ statement ->
                        statement[AllCategoriesTable.categoryImage] = byteArrayString
                    }
                }
            }
        }
        return 1
    }

    override suspend fun getAllCategories(): List<Category> {
        return DatabaseFactory.dbQuery {
            AllCategoriesTable.selectAll().mapNotNull {
                rowToCategory(it)
            }
        }
    }

    override suspend fun getCategoryById(categoryId: Int): Category? {
        return DatabaseFactory.dbQuery {
            AllCategoriesTable.select {
                AllCategoriesTable.categoryId.eq(categoryId)
            }.map {
                rowToCategory(it)
            }.singleOrNull()
        }
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