package com.thanaphoom.api.logging

import com.thanaphoom.dao.DatabaseFactory
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import java.sql.Date

object Loggings : DatabaseFactory.ExtendedIntIdTable("tb_loggings", "logging_id") {
    val messageError = text("message_error")
}
