package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.PartDao
import com.swamisamarthpet.data.model.Machine
import com.swamisamarthpet.data.model.Part
import com.swamisamarthpet.data.tables.PartTable
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class PartRepo(tableName: String): PartDao {

    private val partTable = PartTable(tableName)

    override suspend fun insertPart(partName: String, multiPart: MultiPartData, partDetails: String, machineName: String): Int =
        try{
            var i = 1
            val array = arrayListOf<String>()
            val imagePartArray = arrayListOf<PartData>()
            val partImages = arrayListOf<String>()
            multiPart.forEachPart {
                if(it is PartData.FileItem) {
                    array.add(machineName+partName+i)
                    imagePartArray.add(it)
                    i++
                }
            }

            i = 0
            for(part in imagePartArray){
                if(part is PartData.FileItem) {
                    val name = array[i]
                    val file = File("./build/resources/main/static/$name.png")
                    part.streamProvider().use { its ->
                        file.outputStream().buffered().use {
                            its.copyTo(it)
                        }
                        val bytes = file.readBytes()
                        partImages.add(Base64.getEncoder().encodeToString(bytes))
                    }
                    if(i==imagePartArray.lastIndex){
                        DatabaseFactory.dbQuery {
                            partTable.insert { part ->
                                part[partTable.partName] = partName
                                part[partTable.partImages] = partImages.joinToString(";")
                                part[partTable.partDetails] = partDetails
                            }
                        }
                    }
                }
                i++
                part.dispose()
            }
            1
        }catch (e: Throwable){
            0
        }

    override suspend fun deletePart(partId: Int): Int =
        DatabaseFactory.dbQuery {
            partTable.deleteWhere { partTable.partId.eq(partId) }
        }

    override suspend fun updatePart(partId: Int, multiPart: MultiPartData, partDetails: String, machineName: String): Int =
        try{
            val partName = getPartById(partId)?.partName
            var i = 1
            val array = arrayListOf<String>()
            val imagePartArray = arrayListOf<PartData>()
            val partImages = arrayListOf<String>()
            multiPart.forEachPart {
                if(it is PartData.FileItem) {
                    array.add(machineName+partName+i)
                    imagePartArray.add(it)
                    i++
                }
            }
            i = 0
            for(part in imagePartArray){
                if(part is PartData.FileItem) {
                    val name = array[i]
                    val file = File("./build/resources/main/static/$name.png")
                    part.streamProvider().use { its ->
                        file.outputStream().buffered().use {
                            its.copyTo(it)
                        }
                        val bytes = file.readBytes()
                        partImages.add(Base64.getEncoder().encodeToString(bytes))
                    }
                    if(i==imagePartArray.lastIndex){
                        DatabaseFactory.dbQuery {
                            partTable.update({
                                partTable.partId.eq(partId)
                            }){ statement->
                                statement[partTable.partImages] = partImages.joinToString(";")
                                statement[partTable.partDetails] = partDetails
                            }
                        }
                    }
                }
                i++
                part.dispose()
            }

            1
        }catch (e: Throwable){
            0
        }

    override suspend fun getPartById(partId: Int): Part? =
        DatabaseFactory.dbQuery {
            partTable.select{
                partTable.partId.eq(partId)
            }.map{
                rowToPart(it)
            }.singleOrNull()
        }

    override suspend fun getAllParts(): List<Part> =
        DatabaseFactory.dbQuery {
            partTable.selectAll().mapNotNull {
                rowToPart(it)
            }
        }

    private fun rowToPart(row: ResultRow?): Part? {
        if(row == null)
            return null

        val path = "./build/resources/main/static/"
        val partImages = HashMap<String,ByteArray>()
        val imageNameList = row[partTable.partImages].split(",")
        for(image in imageNameList){
            val imageFile = File("$path$image.png")
            partImages[image] = imageFile.readBytes()
        }

        return Part(
            partId = row[partTable.partId],
            partName = row[partTable.partName],
            partImages = partImages,
            partDetails = row[partTable.partDetails]
        )
    }

}