package com.elksa.sample.buscador.mercadolibre.domain.interfaces

interface ILogger {

    enum class LogLevel {
        VERBOSE, DEBUG, ERROR, WARNING, INFORMATION
    }

    fun log(tag: String?, message: String?, error: Throwable?, logLevel: LogLevel)
}