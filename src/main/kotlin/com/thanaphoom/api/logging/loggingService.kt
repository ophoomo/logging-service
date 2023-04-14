package com.thanaphoom.api.logging

interface LoggingService {
    suspend fun allLogging(): List<Logging>
    suspend fun saveLogging(data: LoggingCreate): Logging?
}