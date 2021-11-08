package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.MachineDao
import com.swamisamarthpet.data.model.Machine
import com.swamisamarthpet.data.tables.MachineTable
import com.swamisamarthpet.data.tables.PartTable
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class MachineRepo(tableName: String): MachineDao {

    private val machineTable = MachineTable(tableName)

    override suspend fun insertMachine(machineName: String, multiPart: MultiPartData, machineDetails: String, machinePdf: String): Int =

        try {
            var i = 1
            val array = arrayListOf<String>()
            val imagePartArray = arrayListOf<PartData>()
            val machineImages = arrayListOf<String>()
            multiPart.forEachPart {
                if(it is PartData.FileItem) {
                    array.add(machineName+i)
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
                        machineImages.add(Base64.getEncoder().encodeToString(bytes))
                    }
                    if(i==imagePartArray.size-1){
                        DatabaseFactory.dbQuery {
                            machineTable.insert { machine ->
                                machine[machineTable.machineName] = machineName
                                machine[machineTable.machineImages] = machineImages.joinToString(";")
                                machine[machineTable.machineDetails] = machineDetails
                                machine[machineTable.machinePdf] = machinePdf
                            }
                        }
                    }
                }
                i++
                part.dispose()
            }

            val table = PartTable(machineName)
            transaction{
                SchemaUtils.create(table)
            }
            1
        }catch (e: Throwable) {
            0
        }


    override suspend fun deleteMachine(machineId: Int): Int =
        DatabaseFactory.dbQuery {
            machineTable.deleteWhere { machineTable.machineId.eq(machineId) }
        }

    override suspend fun updateMachine(machineId: Int,multiPart: MultiPartData, machineDetails: String, machinePdf: String): Int =
        try{
            val machineName = getMachineById(machineId)?.machineName
            var i = 1
            val array = arrayListOf<String>()
            val imagePartArray = arrayListOf<PartData>()
            multiPart.forEachPart {
                if(it is PartData.FileItem) {
                    array.add(machineName+i)
                    imagePartArray.add(it)
                    i++
                }
            }
            val machineImages = array.joinToString(",")
            DatabaseFactory.dbQuery {
                machineTable.update({
                    machineTable.machineId.eq(machineId)
                }){ statement ->
                    statement[machineTable.machineImages] = machineImages
                    statement[machineTable.machineDetails] = machineDetails
                    statement[machineTable.machinePdf] = machinePdf
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
                    }
                }
                i++
                part.dispose()
            }
            1
        }catch (e: Throwable){
            0
        }

    override suspend fun getMachineById(machineId: Int): Machine? =
        DatabaseFactory.dbQuery {
            machineTable.select{
                machineTable.machineId.eq(machineId)
            }.map {
                rowToMachine(it)
            }.singleOrNull()
        }

    override suspend fun getAllMachines(): List<Machine> =
        DatabaseFactory.dbQuery{
            machineTable.selectAll().mapNotNull {
                rowToMachine(it)
            }
        }

    private fun rowToMachine(row: ResultRow?): Machine? {
        if(row == null)
            return null

        val machineImages = HashMap<Int,ByteArray>()
        val images = row[machineTable.machineImages].split(";")
        for(i in 1..images.size){
            val imageByteArray = Base64.getDecoder().decode(images[i-1])
            machineImages[i]=imageByteArray
        }

        return Machine(
            machineId = row[machineTable.machineId],
            machineName = row[machineTable.machineName],
            machineImages = machineImages,
            machineDetails = row[machineTable.machineDetails],
            machinePdf = row[machineTable.machinePdf]
        )
    }

}