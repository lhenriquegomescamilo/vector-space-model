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
            vectors.map { async { calculoSqrt(it) } }.awaitAll()
            val sumA = calculatingValue(vectors.clone())
            val baseOfCalc = vectors.map { async { calculoSqrt(it) } }.awaitAll()
            val result = baseOfCalc.reduce { acc: Double, current: Double -> acc * current }
            sumA.div(result)
        }

        private suspend fun calculatingValue(vectors: Array<Array<Int>>) = coroutineScope {
            var sum = 0
            vectors.sortBy { it.size }
            val y = vectors.takeLast(1).first()
            val newVectors = vectors.dropLast(1)
            for ((index, valueA) in y.withIndex()) {
                for (vector in newVectors) {
                    sum += calculateTheBase(vector, index, valueA)
                }
            }
            sum
        }

        private fun calculateTheBase(vector: Array<Int>, index: Int, valueA: Int): Int = try {
            val  valueB: Int = vector.get(index)
             (valueA * valueB)
        } catch (e: Exception) {
             (valueA * 1)
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
