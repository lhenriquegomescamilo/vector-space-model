package com.camilo.main

import com.sun.javaws.exceptions.InvalidArgumentException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.lang.Math.sqrt
import java.util.*

@Suppress("UNCHECKED_CAST")
class CosineSimilarity {
    companion object {
        suspend fun calculate(vectors: Array<Array<Int>>) = coroutineScope {
            if (vectors.size <= 1) throw InvalidArgumentException(arrayOf("This values has be two vectors"))
//            if (!y.size.equals(z.size)) throw InvalidArgumentException(arrayOf("The param Y and Z should be equal"))

            vectors.map { async { calculoSqrt(it) } }.awaitAll()
            val sumA = calculatingValue(vectors.clone())
            val baseOfCalc = vectors.map { async { calculoSqrt(it) } }.awaitAll()
            val result = baseOfCalc.reduce { acc: Double, current: Double -> acc * current }
            sumA.div(result)
        }

        private suspend fun calculatingValue(vectors: Array<Array<Int>>) = coroutineScope {
            var sum = 0
            vectors.sortByDescending { it.size }
            val y = vectors.takeLast(1).first()
            val newVectors = vectors.dropLast(1)
            for ((index, valueA) in y.withIndex()) {
                for (vector in newVectors) {
                    var valueB: Int = vector.get(index)
                    if(Objects.isNull(valueB)) valueB = 0
                    sum += (valueA * valueB)
                }
            }
            sum
        }

        private fun orderByBigVector(vectors: Array<Array<Int>>): Array<Array<Int>> {
            vectors.sortByDescending { it.size }
            return vectors
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
