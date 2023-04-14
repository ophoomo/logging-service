package com.thanaphoom.api.logging

import com.thanaphoom.dao.DatabaseFactory
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID


@Serializable
data class LoggingCreate(val messageError: String)
@Serializable
data class Logging(val loggingId: Int, val messageError: String)

object Loggings : DatabaseFactory.ExtendedIntIdTable("tb_loggings", "logging_id") {
    val messageError = text("message_error")
}
