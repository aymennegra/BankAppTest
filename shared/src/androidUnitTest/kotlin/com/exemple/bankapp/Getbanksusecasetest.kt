package com.example.bankapp

import com.example.bankapp.domain.model.Bank
import com.example.bankapp.domain.repository.BankRepository
import com.example.bankapp.domain.usecase.GetBanksUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GetBanksUseCaseTest {

    private val repository = mockk<BankRepository>()
    private val useCase = GetBanksUseCase(repository)

    @Test
    fun `should return banks from repository`() = runBlocking {
        val banks = listOf(
            Bank("CA Languedoc", true, emptyList()),
            Bank("Boursorama", false, emptyList())
        )

        every { repository.getBanks() } returns flowOf(Result.success(banks))

        val result = useCase.getBanks().first()

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals(2, data.size)
        assertEquals("CA Languedoc", data[0].name)
        assertEquals("Boursorama", data[1].name)

        verify(exactly = 1) { repository.getBanks() }
    }

    @Test
    fun `should return error when repository fails`() = runBlocking {
        val exception = Exception("Network error")
        every { repository.getBanks() } returns flowOf(Result.failure(exception))

        val result = useCase.getBanks().first()

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)

        verify(exactly = 1) { repository.getBanks() }
    }

    @Test
    fun `should return empty list when repository returns empty`() = runBlocking {
        every { repository.getBanks() } returns flowOf(Result.success(emptyList()))

        val result = useCase.getBanks().first()

        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals(0, data.size)

        verify(exactly = 1) { repository.getBanks() }
    }
}
