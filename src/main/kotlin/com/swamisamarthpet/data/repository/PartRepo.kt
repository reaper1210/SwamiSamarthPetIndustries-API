package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.PartDao
import com.swamisamarthpet.data.model.Machine
import com.swamisamarthpet.data.model.Part
import com.swamisamarthpet.data.tables.PartTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class PartRepo(tableName: String): PartDao {

    private val partTable = PartTable(tableName)

    override suspend fun insertPart(partName: String, partImage: String, partDetails: String): Part? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = partTable.insert { part ->
                part[partTable.partName] = partName
                part[partTable.partImage] = partImage
                part[partTable.partDetails] = partDetails
            }
        }
        return rowToPart(statement?.resultedValues?.get(0))
    }

    override suspend fun deletePart(partId: Int): Int =
        DatabaseFactory.dbQuery {
            partTable.deleteWhere { partTable.partId.eq(partId) }
        }

    override suspend fun updatePart(partId: Int, partImage: String, partDetails: String): Int =
        DatabaseFactory.dbQuery {
            partTable.update({
                partTable.partId.eq(partId)
            }){ statement->
                statement[partTable.partImage] = partImage
                statement[partTable.partDetails] = partDetails
            }
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
        return Part(
            partId = row[partTable.partId],
            partName = row[partTable.partName],
            partImage= row[partTable.partImage],
            partDetails = row[partTable.partDetails]
        )
    }

}