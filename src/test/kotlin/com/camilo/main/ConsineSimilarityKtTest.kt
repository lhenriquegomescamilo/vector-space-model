package com.camilo.main

import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ConsineSimilarityKtTest {

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
    fun `The cosine similaratiy should be same in the two vectors`() = runBlocking {
        val expected = 0.7857142857142857
        val y = arrayOf(2, 1, 3)
        val z = arrayOf(1, 3, 2)
        val cosineSimilarityValue = CosineSimilarity.calculate(arrayOf(y, z))
        assertEquals(expected, cosineSimilarityValue)

    }

    @Test
    fun `The cosine similaratiy should be same in the three vectors`() = runBlocking {
        val expected = 0.4772522177007578
        val y = arrayOf(2, 1, 3)
        val z = arrayOf(1, 3, 2)
        val x = arrayOf(1, 3, 2)
        val cosineSimilarityValue = CosineSimilarity.calculate(arrayOf(y, z, x))
        assertEquals(expected, cosineSimilarityValue)
    }

    @Test
    fun `The cosine similaratiy should be same in the three vectors with diferent size`() = runBlocking {
        val expected = 0.4003203845127178
        val y = arrayOf(2, 1, 3)
        val z = arrayOf(1, 3, 2)
        val x = arrayOf(1, 3, 2, 5)
        val cosineSimilarityValue = CosineSimilarity.calculate(arrayOf(y, z, x))
        assertEquals(expected, cosineSimilarityValue)
    }
}
