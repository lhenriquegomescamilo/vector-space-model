package com.camilo.main

import com.camilo.main.model.CosineSimiratiyByFile
import kotlinx.coroutines.*
import java.io.File
import java.io.FileReader

class IndiceFileGenerator {
    suspend fun readWordsOfFile(file: File) = withContext(Dispatchers.IO) {
        FileReader(file).use {
            it.readLines()
                .asSequence()
                .flatMap { line -> line.split(" ").asSequence() }
                .toList()
        }
    }

    suspend fun createIndiceForFile(file: File) = coroutineScope {
        val words = readWordsOfFile(file)
        words.asSequence()
            .groupBy { it }
            .map { it.key to it.value.size }
            .toMap()
    }

    suspend fun createIndiceForQuery(query: String) = coroutineScope {
        query.split(" ").asSequence()
            .map { it.toLowerCase() }
            .groupBy { it }
            .map { it.key to it.value.size }
            .toMap()

    }

    suspend fun orderFileByIndiceUsingQuery(files: Array<File>, query: String) = coroutineScope {
        val vectorQuery = createIndiceForQuery(query)
        val filesWithIndiceByQueryDeferred = files.map {
            async { calculateCosineByFile(it) }
        }

        val filesWithIndiceByQuery = filesWithIndiceByQueryDeferred.awaitAll()
        val listQuery = createWhenHaveOneDimension(vectorQuery)
        val values = listQuery.map { it.second.toTypedArray() }
        val vectors = values.toTypedArray()
        val cosineOfQuery = CosineSimilarity.calculate(vectors)
        filesWithIndiceByQuery
            .sortedWith(compareBy(
                { valueNearOfQuery(it.cosine, cosineOfQuery) },
                { byMainWordOfQuery(it, vectorQuery) }
            ))
            .asReversed()
    }

    private suspend fun IndiceFileGenerator.calculateCosineByFile(it: File): CosineSimiratiyByFile {
        val vectorOfFile = createIndiceForFile(file = it)
        val vectorWithList = createWhenHaveOneDimension(vectorOfFile)
        val values = vectorWithList.map { it.second.toTypedArray() }
        val vectors = values.toTypedArray()
        val cosine = CosineSimilarity.calculate(vectors)
        return CosineSimiratiyByFile(cosine = cosine, nameFile = it.name, indices = vectorOfFile)
    }

    private fun byMainWordOfQuery(it: CosineSimiratiyByFile, vectorQuery: Map<String, Int>): Int {
        return it.indices
            .filter { item ->
                item.key == vectorQuery.entries
                    .sortedByDescending { v -> v.value }
                    .first().key
            }
            .entries.first().value
    }

    private fun createWhenHaveOneDimension(vectorOfFile: Map<String, Int>): List<Pair<String, MutableList<Int>>> {
        return vectorOfFile
            .asSequence()
            .map { it.key to mutableListOf(it.value) }
            .map { pair ->
                pair.second.add(1)
                pair
            }
            .toList()
    }

    fun valueNearOfQuery(value: Double, cosineOfQuery: Double): Double {
        val result = cosineOfQuery - value
        return if (result < 0) {
            result * (-1)
        } else {
            result
        }
    }
}
