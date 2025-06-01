package com.vivek.basictemplate.data

import com.vivek.basictemplate.domain.Book
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class DefaultBookRepositoryTest : KoinTest {

    private val mockApiService: BookApiService = mockk()
    private val mockDefaultBookRepository: DefaultBookRepository by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockApiService }
                single { DefaultBookRepository(get()) }
            }
        )
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `getData successfully fetches from ApiService and returns success Result`() = runTest {
        // Given
        val bookApiModels = listOf(BookApiModel(1, "Success", "ss"))
        val expectedBooks = listOf(Book(1, "Success"))
        coEvery { mockApiService.getBooks() } returns Result.success(bookApiModels)

        // When
        val result = mockDefaultBookRepository.getBooks()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedBooks, result.getOrNull())
        coVerify(exactly = 1) { mockApiService.getBooks() }
    }

    @Test
    fun `getData handles ApiService exception and returns failure Result`() = runTest {
        // Given
        val apiException = Exception("API Error")
        coEvery { mockApiService.getBooks() } returns Result.failure(apiException)

        // When
        val result = mockDefaultBookRepository.getBooks()

        // Then
        assertTrue(result.isFailure)
        assertEquals(apiException, result.exceptionOrNull())
        coVerify(exactly = 1) { mockApiService.getBooks() }
    }
}