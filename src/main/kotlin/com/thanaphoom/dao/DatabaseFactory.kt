package com.thanaphoom.dao

import com.thanaphoom.api.logging.Loggings
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.time.LocalDateTime

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("storage.driverClassName").getString()
        val jdbcURL = config.property("storage.jdbcURL").getString()
        val dataSource = createHikariDataSource(url = jdbcURL, driver = driverClassName)
        val database = Database.connect(dataSource)
        transaction(database) {
            SchemaUtils.create(Loggings)
        }
        Flyway.configure().dataSource(dataSource).load().migrate()
    }

    private fun createHikariDataSource(
        url: String,
        driver: String,
    ) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    abstract class ExtendedIntIdTable(name: String, id: String) : IntIdTable(name, id) {

        val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
        val updatedAt = datetime("updatedAt").nullable()
        val deletedAt = datetime("deletedAt").nullable()

    }

    abstract class ExtendedIntEntity(id: EntityID<Int>, table: ExtendedIntIdTable) : IntEntity(id) {
        val createdAt by table.createdAt
        var updatedAt by table.updatedAt
        var deletedAt by table.deletedAt

        override fun delete() {
            deletedAt = LocalDateTime.now()
        }
    }

    abstract class ExtendedIntEntityClass<E:ExtendedIntEntity>(table: ExtendedIntIdTable) : IntEntityClass<E>(table) {
        init {
            EntityHook.subscribe { action ->
                if (action.changeType == EntityChangeType.Updated)
                    action.toEntity(this)?.updatedAt = LocalDateTime.now()
            }
        }
    }
}