package com.elksa.sample.buscador.mercadolibre.framework.android

import javax.inject.Inject
import kotlin.random.Random

class RandomGenerator @Inject constructor() {

    fun generateRandomInt(from: Int, to: Int) = Random.nextInt(from, to)
}