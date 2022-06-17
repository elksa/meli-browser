package com.elksa.sample.buscador.mercadolibre.framework.android

import android.util.Log
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger.LogLevel
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger.LogLevel.DEBUG
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger.LogLevel.INFORMATION
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger.LogLevel.VERBOSE
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger.LogLevel.WARNING
import javax.inject.Inject

class LogcatLogger @Inject constructor() : ILogger {

    override fun log(tag: String?, message: String?, error: Throwable?, logLevel: LogLevel) {
        when (logLevel) {
            VERBOSE -> Log.v(tag, message, error)
            DEBUG -> Log.d(tag, message, error)
            ERROR -> Log.e(tag, message, error)
            WARNING -> Log.w(tag, message, error)
            INFORMATION -> Log.i(tag, message, error)
        }
    }
}