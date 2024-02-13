package com.elksa.sample.buscador.mercadolibre.utils

import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
fun <T> getField(target: Any, fieldName: String): T {
    val field = target::class.java.getDeclaredField(fieldName)
    field.isAccessible = true
    return field.get(target) as T
}

fun <V, T : Any> setField(fieldName: String, value: V, target: T) {
    val field = target::class.java.getDeclaredField(fieldName)
    field.isAccessible = true
    field.set(target, value)
}

inline fun <reified T> T.callPrivateFun(name: String, vararg args: Any?): Any? =
    T::class.declaredMemberFunctions
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }
        ?.call(this, *args)