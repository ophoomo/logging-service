package com.thanaphoom

import com.thanaphoom.dao.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.thanaphoom.plugins.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.compression.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(environment.config)
    install(Compression)
    install(CachingHeaders)
    configureRouting()
    configureSerialization()
}
