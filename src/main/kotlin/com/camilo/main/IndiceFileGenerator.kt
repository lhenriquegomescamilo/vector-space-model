package com.camilo.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
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

    suspend fun indice(file: File) = coroutineScope {
        val words = readWordsOfFile(file)
        words.asSequence()
            .groupBy { it }
            .map { it.key to it.value.size }
            .toMap()
    }
}
