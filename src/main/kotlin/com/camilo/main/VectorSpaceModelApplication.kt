package com.camilo.main

import kotlinx.coroutines.coroutineScope

suspend fun main(args: Array<String>) = coroutineScope {
    val y = arrayOf(2, 1, 3)
    val z = arrayOf(1, 3, 2)

    val cosineSimilarity = CosineSimilarity.calculate(y, z)
    println("The value is $cosineSimilarity")
}
