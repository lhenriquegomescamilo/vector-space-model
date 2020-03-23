package com.camilo.main

import com.sun.javaws.exceptions.InvalidArgumentException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.sqrt

suspend fun main(args: Array<String>) = coroutineScope {
    val y = arrayOf(2, 1, 3)
    val z = arrayOf(1, 3, 2)

    val cosineSimilarity = cosineSimilarity(y, z)
    println("The value is $cosineSimilarity")
}

suspend fun cosineSimilarity(y: Array<Int>, z: Array<Int>) = coroutineScope {
    if (!y.size.equals(z.size)) throw InvalidArgumentException(arrayOf("The param Y and Z should be equal"))
    val sumA = calculatingValue(y, z)
    val sumB = async { calculoSqrt(y) }
    val sumC = async { calculoSqrt(z) }
    sumA.div(sumB.await() * sumC.await())

}


suspend fun calculatingValue(y: Array<Int>, z: Array<Int>) = coroutineScope {
    var sum = 0
    for ((index, value) in y.withIndex()) {
        val valueB = z[index]
        sum += (value * valueB)
    }
    sum
}

suspend fun calculoSqrt(y: Array<Int>) = coroutineScope {
    var sum = 0.0
    for (i in y) {
        sum += Math.pow(i.toDouble(), 2.0)
    }
    sqrt(sum)
}

