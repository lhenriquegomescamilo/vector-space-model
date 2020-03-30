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
    fun `should read words of file`() = runBlocking {
        val indiceFile = IndiceFileGenerator()
        val expectedWords = 5
        val file = readFile("files/document_01.txt")
        val quantityOfWords = indiceFile.readWordsOfFile(file)
        Assertions.assertEquals(expectedWords, quantityOfWords.size)
    }

    private fun CoroutineScope.readFile(path: String): File {
        val filePath = this.javaClass.classLoader?.getResource(path)?.file ?: ""
        val file = File(filePath)
        return file
    }

    @Test
    fun `Should create indices for documento 01`() = runBlocking {
        val generator = IndiceFileGenerator()
        val file = readFile("files/document_01.txt")
        val indicesExpected = hashMapOf("apple" to 4, "banana" to 1)
        val indices = generator.createIndiceForFile(file)
        Assertions.assertEquals(indicesExpected, indices)

    }


    @Test
    fun `Should create indices for documento 02`() = runBlocking {
        val generator = IndiceFileGenerator()
        val file = readFile("files/document_02.txt")
        val indicesExpected = hashMapOf("apple" to 1, "banana" to 3)
        val indices = generator.createIndiceForFile(file)
        Assertions.assertEquals(indicesExpected, indices)
    }

    @Test
    fun `Should create indices for documento 03`() = runBlocking {
        val generator = IndiceFileGenerator()
        val file = readFile("files/document_03.txt")
        val indicesExpected = hashMapOf("banana" to 4)
        val indices = generator.createIndiceForFile(file)
        Assertions.assertEquals(indicesExpected, indices)
    }

    @Test
    fun `Should create indices for query`() = runBlocking {
        val generator = IndiceFileGenerator()
        val phrase = "Banana apple"
        val indicesExpected = hashMapOf("banana" to 1, "apple" to 1)
        val indices = generator.createIndiceForQuery(phrase)
        Assertions.assertEquals(indicesExpected, indices)
    }

    @Test
    fun `Should create consine similiratiy by file`() = runBlocking {
        val generator = IndiceFileGenerator()
        val files = arrayOf(
            readFile("files/document_01.txt"),
            readFile("files/document_02.txt"),
            readFile("files/document_03.txt")
        )
        val phrase = "Banana"
        val indices = generator.orderFileByIndiceUsingQuery(files, phrase)
        val namesOrdered = arrayOf("document_03.txt", "document_02.txt", "document_01.txt")
        Assertions.assertTrue(
            indices.all { indice -> namesOrdered.find { nameFile -> nameFile == indice.nameFile }?.isNotEmpty()!! }
        )
    }

}
