package com.swamisamarthpet

import com.swamisamarthpet.data.tables.AllCategoriesTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    fun init(){
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(AllCategoriesTable)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
//        config.jdbcUrl = System.getenv("DATABASE_URL")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        val uri = URI(System.getenv("DATABASE_URL"))
        val username = System.getenv("DB_USERNAME")
        val password = System.getenv("DB_PASSWORD")

        config.jdbcUrl = "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require" + "&user=$username&password=$password"

        return HikariDataSource(config)
    }

    suspend fun<T> dbQuery(block:()-> T):T =
        withContext(Dispatchers.IO){
            transaction {
                block()
            }
        }

}