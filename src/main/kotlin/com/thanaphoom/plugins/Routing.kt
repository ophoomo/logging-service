package com.thanaphoom.plugins

import com.thanaphoom.api.logging.loggingController
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("true")
        }
    }
    loggingController()
}
