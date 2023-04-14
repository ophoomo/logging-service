package com.thanaphoom.api.logging

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loggingController() {
    val dto: LoggingServiceImpl = LoggingServiceImpl().apply {  }
    routing {
        route("/logging") {
            get("/") {
                val data = dto.allLogging()
                call.respond(HttpStatusCode.OK, data)
            }
            post("/") {
                val bodyData = call.receive<LoggingCreate>()
                val insert = dto.saveLogging(bodyData)
                if (insert != null)
                call.respond(HttpStatusCode.Created, insert)
            }
        }
    }
}