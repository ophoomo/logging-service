package com.thanaphoom.api.logging

import com.thanaphoom.dao.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class LoggingServiceImpl  : LoggingService  {
    private fun resultRowToLogging(row: ResultRow) = Logging(
        loggingId = row[Loggings.id].value,
        messageError = row[Loggings.messageError],
    )
    override suspend fun allLogging(): List<Logging> {
        return dbQuery {
            Loggings.selectAll().map(::resultRowToLogging)
        }
    }

    override suspend fun saveLogging(data: LoggingCreate): Logging? {
        return dbQuery {
            val insert = Loggings.insert {
                it[Loggings.messageError] = data.messageError
            }
            insert.resultedValues?.singleOrNull()?.let(::resultRowToLogging)
        }
    }
}