package com.camilo.main

import com.sun.javaws.exceptions.InvalidArgumentException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Math.sqrt

class CosineSimilarity {
    companion object {
        suspend fun calculate(y: Array<Int>, z: Array<Int>) = coroutineScope {
            if (!y.size.equals(z.size)) throw InvalidArgumentException(arrayOf("The param Y and Z should be equal"))
            val sumA = calculatingValue(y, z)
            val sumB = async { calculoSqrt(y) }
            val sumC = async { calculoSqrt(z) }
            sumA.div(sumB.await() * sumC.await())
        }

        private suspend fun calculatingValue(y: Array<Int>, z: Array<Int>) = coroutineScope {
            var sum = 0
            for ((index, value) in y.withIndex()) {
                val valueB = z[index]
                sum += (value * valueB)
            }
            sum
        }

        private suspend fun calculoSqrt(y: Array<Int>) = coroutineScope {
            var sum = 0.0
            for (i in y) {
                sum += Math.pow(i.toDouble(), 2.0)
            }
            sqrt(sum)
        }
    }


}
