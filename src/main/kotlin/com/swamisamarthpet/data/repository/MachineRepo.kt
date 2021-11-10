package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.MachineDao
import com.swamisamarthpet.data.model.Machine
import com.swamisamarthpet.data.tables.MachineTable
import com.swamisamarthpet.data.tables.PartTable
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import java.util.zip.Deflater
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
                        machineImages.add(bos.toByteArray().contentToString())
                    }
                    if(i==imagePartArray.lastIndex){
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
            val machineImages = arrayListOf<String>()

            i = 0
            for(part in imagePartArray){
                if(part is PartData.FileItem) {
                    val name = array[i]
                    val file = File("./build/resources/main/static/$name.png")
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
                        machineImages.add(bos.toByteArray().contentToString())

                        if(i==imagePartArray.lastIndex){
                            DatabaseFactory.dbQuery {
                                machineTable.update({
                                    machineTable.machineId.eq(machineId)
                                }){ statement ->
                                    statement[machineTable.machineImages] = machineImages.joinToString(";")
                                    statement[machineTable.machineDetails] = machineDetails
                                    statement[machineTable.machinePdf] = machinePdf
                                }
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

    override suspend fun getMachineById(machineId: Int): Machine? =
        DatabaseFactory.dbQuery {
            machineTable.select{
                machineTable.machineId.eq(machineId)
            }.map {
                rowToMachine(it)
            }.singleOrNull()
        }

    override suspend fun getAllMachines(): List<HashMap<String,String>>{
        val machineHashMap = ArrayList<HashMap<String,String>>()
        val rawList = DatabaseFactory.dbQuery{
            machineTable.selectAll().mapNotNull {
                rowToMachine(it)
            }
        }
        for(rawMachine in rawList){
            val imageList = rawMachine.machineImages.split(";")
            val machine = HashMap<String,String>()
            machine["machineId"] = rawMachine.machineId.toString()
            machine["machineName"] = rawMachine.machineName
            machine["machineImage"] = imageList[0]
            machineHashMap.add(machine)
        }
        return machineHashMap
    }

    private fun rowToMachine(row: ResultRow?): Machine? {
        if(row == null)
            return null

        return Machine(
            machineId = row[machineTable.machineId],
            machineName = row[machineTable.machineName],
            machineImages = row[machineTable.machineImages],
            machineDetails = row[machineTable.machineDetails],
            machinePdf = row[machineTable.machinePdf]
        )
    }

}