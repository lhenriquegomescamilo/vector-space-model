package com.camilo.main

import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class IndiceFileTest {

    private val mainThreadSurrogate = newSingleThreadContext("The main thread")

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }


    @Test
    fun `must have file`() = runBlocking {
        val indiceFile = IndiceFileGenerator()
        val expectedWords = 5
        val file = readFile()
        val quantityOfWords = indiceFile.readWordsOfFile(file)
        Assertions.assertEquals(expectedWords, quantityOfWords.size)
    }

    private fun CoroutineScope.readFile(): File {
        val filePath = this.javaClass.classLoader?.getResource("files/document_01.txt")?.file ?: ""
        val file = File(filePath)
        return file
    }

    @Test
    fun `Should create indices`() = runBlocking {
        val generator = IndiceFileGenerator()
        val file = readFile()
        val indicesExpected = hashMapOf("apple" to 4, "banana" to 1)
        val indices = generator.indice(file)
        Assertions.assertEquals(indicesExpected, indices)

    }
}
