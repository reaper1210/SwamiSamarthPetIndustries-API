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

    override suspend fun insertMachine(machineName: String, multiPart: MultiPartData, machineDetails: String, youtubeVideoLink: String): Int {
        var machinePdf = ""
        val machineImages = arrayListOf<String>()
        val multiPartList = arrayListOf<PartData>()
        multiPart.forEachPart {
            if(it is PartData.FileItem){
                multiPartList.add(it)
            }
        }

        multiPartList.forEachIndexed { index, part->
            if(part is PartData.FileItem) {
                val file = if(index!=0){
                    File("./build/resources/main/static/${machineName}${index}.png")
                }
                else{
                    File("./build/resources/main/static/${machineName}Pdf.pdf")
                }

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

                    if(index!=0){
                        machineImages.add(bos.toByteArray().contentToString())
                    }
                    else{
                        machinePdf = bos.toByteArray().contentToString()
                    }

                    if(index==multiPartList.lastIndex){
                        DatabaseFactory.dbQuery {
                            machineTable.insert { machine ->
                                machine[machineTable.machineName] = machineName
                                machine[machineTable.machineImages] = machineImages.joinToString(";")
                                machine[machineTable.machineDetails] = machineDetails
                                machine[machineTable.machinePdf] = machinePdf
                                machine[machineTable.youtubeVideoLink] = youtubeVideoLink
                            }
                        }
                    }
                    file.delete()
                }

            }
            part.dispose()
        }

        val table = PartTable(machineName)
        transaction{
            SchemaUtils.create(table)
        }
        return 1
    }

    override suspend fun deleteMachine(machineId: Int): Int {
        return DatabaseFactory.dbQuery {
            machineTable.deleteWhere { machineTable.machineId.eq(machineId) }
        }
    }

    override suspend fun updateMachine(machineId: Int,multiPart: MultiPartData, machineDetails: String, youtubeVideoLink: String): Int {
        val machineName = getMachineById(machineId)?.machineName
        var machinePdf = ""
        val machineImages = arrayListOf<String>()
        val multiPartList = arrayListOf<PartData>()
        multiPart.forEachPart {
            if(it is PartData.FileItem){
                multiPartList.add(it)
            }
        }

        multiPartList.forEachIndexed { index, part->
            if(part is PartData.FileItem) {
                val file = if(index!=0){
                    File("./build/resources/main/static/${machineName}${index}.png")
                }
                else{
                    File("./build/resources/main/static/${machineName}Pdf.pdf")
                }

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

                    if(index!=0){
                        machineImages.add(bos.toByteArray().contentToString())
                    }
                    else{
                        machinePdf = bos.toByteArray().contentToString()
                    }

                    if(index==multiPartList.lastIndex){
                        DatabaseFactory.dbQuery {
                            machineTable.update({
                                machineTable.machineId.eq(machineId)
                            }){ statement ->
                                statement[machineTable.machineImages] = machineImages.joinToString(";")
                                statement[machineTable.machineDetails] = machineDetails
                                statement[machineTable.machinePdf] = machinePdf
                                statement[machineTable.youtubeVideoLink] = youtubeVideoLink
                            }
                        }
                    }
                    file.delete()
                }
            }
            part.dispose()
        }
        return 1
    }

    override suspend fun getMachineById(machineId: Int): Machine? {
        return DatabaseFactory.dbQuery {
            machineTable.select {
                machineTable.machineId.eq(machineId)
            }.map {
                rowToMachine(it)
            }.singleOrNull()
        }
    }

    override suspend fun getAllMachines(): List<HashMap<String,String>> {
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

    fun rowToMachine(row: ResultRow?): Machine? {
        if(row == null)
            return null

        return Machine(
            machineId = row[machineTable.machineId],
            machineName = row[machineTable.machineName],
            machineImages = row[machineTable.machineImages],
            youtubeVideoLink = row[machineTable.youtubeVideoLink],
            machineDetails = row[machineTable.machineDetails],
            machinePdf = row[machineTable.machinePdf]
        )
    }

}