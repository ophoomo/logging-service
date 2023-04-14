package com.thanaphoom

import com.thanaphoom.api.logging.loggingController
import com.thanaphoom.dao.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.thanaphoom.plugins.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.requestvalidation.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(environment.config)
    install(Compression)
    install(CachingHeaders)
    install(RequestValidation)
    configureSerialization()
    configureRouting()
}
