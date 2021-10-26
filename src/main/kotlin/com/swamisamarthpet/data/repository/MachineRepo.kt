package com.swamisamarthpet.data.repository

import com.swamisamarthpet.DatabaseFactory
import com.swamisamarthpet.data.dao.MachineDao
import com.swamisamarthpet.data.model.Machine
import com.swamisamarthpet.data.tables.MachineTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class MachineRepo(tableName: String): MachineDao {

    private val machineTable = MachineTable(tableName)

    override suspend fun insertMachine(machineName: String, machineImage: String, machineDetails: String, machinePdf: String): Machine? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = machineTable.insert { machine ->
                machine[machineTable.machineName] = machineName
                machine[machineTable.machineImage] = machineImage
                machine[machineTable.machineDetails] = machineDetails
                machine[machineTable.machinePdf] = machinePdf
            }
        }
        return rowToMachine(statement?.resultedValues?.get(0))
    }

    override suspend fun deleteMachine(machineId: Int): Int =
        DatabaseFactory.dbQuery {
            machineTable.deleteWhere { machineTable.machineId.eq(machineId) }
        }

    override suspend fun updateMachine(machineId: Int, machineImage: String, machineDetails: String, machinePdf: String): Int =
        DatabaseFactory.dbQuery {
            machineTable.update({
                machineTable.machineId.eq(machineId)
            }){ statement ->
                statement[machineTable.machineImage] = machineImage
                statement[machineTable.machineDetails] = machineDetails
                statement[machineTable.machinePdf] = machinePdf
            }
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
        return Machine(
            machineId = row[machineTable.machineId],
            machineName = row[machineTable.machineName],
            machineImage = row[machineTable.machineImage],
            machineDetails = row[machineTable.machineDetails],
            machinePdf = row[machineTable.machinePdf]
        )
    }

}