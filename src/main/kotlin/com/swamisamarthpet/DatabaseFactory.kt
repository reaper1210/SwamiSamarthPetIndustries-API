package com.swamisamarthpet

import com.swamisamarthpet.data.tables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    fun init(){
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(AllCategoriesTable)
            SchemaUtils.create(RegisteredUsersTable)
            SchemaUtils.create(PopularProductsTable)
            SchemaUtils.create(BannersTable)
            SchemaUtils.create(RatingsTable)
            RatingsTable.insert { ratings->
                ratings[oneStars] = 0
                ratings[twoStars] = 0
                ratings[threeStars] = 0
                ratings[fourStars] = 0
                ratings[fiveStars] = 0
            }
        }
    }

    private fun hikari(): HikariDataSource {

        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
//         config.jdbcUrl = System.getenv("DATABASE_URL")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

       val uri = URI(System.getenv("DATABASE_URL"))
       val username = uri.userInfo.split(":").toTypedArray()[0]
       val password = uri.userInfo.split(":").toTypedArray()[1]
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
