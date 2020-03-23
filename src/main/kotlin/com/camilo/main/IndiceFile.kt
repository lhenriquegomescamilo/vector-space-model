package com.camilo.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader

class IndiceFile {
    suspend fun readWordsOfFile(file: File) = withContext(Dispatchers.IO) {
        FileReader(file).use {
            it.readLines()
                .asSequence()
                .flatMap { line -> line.split(" ").asSequence() }
                .toList()
        }
    }
}
