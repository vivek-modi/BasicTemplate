package com.vivek.basictemplate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vivek.basictemplate.domain.Book
import com.vivek.basictemplate.domain.BookRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MainActivityViewModelTest : KoinTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val mockBookRepository: BookRepository = mockk()
    private val viewModel: MainViewModel by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockBookRepository }
                viewModel { MainViewModel(get()) }
            }
        )
    }

    @Before
    fun setUp() {
        // Optional: common setup before each test
    }

    @After
    fun tearDown() {
        stopKoin() // Important to stop Koin after each test to avoid interference
        unmockkAll() // Clear all mocks
    }

    @Test
    fun `fetchData success updates LiveData correctly`() = runTest {
        // Given
        val fakeData = listOf(Book(1, "Test Title"))
        coEvery { mockBookRepository.getBooks() } returns Result.success(fakeData)

        // WHEN
        viewModel.fetchData()

        // THEN
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(fakeData, viewModel.data.value)
        assertNull(viewModel.error.value)
        coVerify { mockBookRepository.getBooks() }
    }

    @Test
    fun `fetchData failure updates LiveData correctly`() = runTest {
        // GIVEN
        val errorMessage = "Newtwork Error"
        val exception = RuntimeException(errorMessage)
        coEvery { mockBookRepository.getBooks() } returns Result.failure(exception)

        // WHEN
        viewModel.fetchData()

        // THEN
        assertEquals(false, viewModel.isLoading.value)
        assertNull(viewModel.data.value)
        assertEquals(errorMessage, viewModel.error.value)
        coVerify { mockBookRepository.getBooks() }
    }
}