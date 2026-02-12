package com.exemple.bankapp


import app.cash.turbine.test
import com.example.bankapp.data.remote.BankApiService
import com.example.bankapp.data.remote.BankDto
import com.example.bankapp.data.repository.BankRepositoryImpl
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BankRepositoryImplTest {

    private val apiService = mockk<BankApiService>()
    private val repository = BankRepositoryImpl(apiService)

    @Test
    fun `should emit success when API call succeeds`() = runTest {
        // Given
        val banksDto = listOf(
            BankDto(name = "CA Languedoc", isCA = 1, accounts = emptyList()),
            BankDto(name = "Boursorama", isCA = 0, accounts = emptyList())
        )
        coEvery { apiService.getBanks() } returns banksDto

        // When & Then
        repository.getBanks().test {
            val result = awaitItem()

            result.isSuccess shouldBe true
            val banks = result.getOrNull()!!
            banks.size shouldBe 2
            banks[0].name shouldBe "CA Languedoc"
            banks[0].isCA shouldBe true
            banks[1].name shouldBe "Boursorama"
            banks[1].isCA shouldBe false

            awaitComplete()
        }

        coVerify(exactly = 1) { apiService.getBanks() }
    }

    @Test
    fun `should emit failure when API call fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { apiService.getBanks() } throws exception

        // When & Then
        repository.getBanks().test {
            val result = awaitItem()

            result.isFailure shouldBe true
            result.exceptionOrNull()?.message shouldBe "Network error"

            awaitComplete()
        }

        coVerify(exactly = 1) { apiService.getBanks() }
    }

    @Test
    fun `should convert DTO to Domain correctly`() = runTest {
        // Given
        val banksDto = listOf(
            BankDto(name = "Test Bank", isCA = 1, accounts = emptyList())
        )
        coEvery { apiService.getBanks() } returns banksDto

        // When & Then
        repository.getBanks().test {
            val result = awaitItem()
            val bank = result.getOrNull()!![0]

            // Verify DTO to Domain conversion
            bank.name shouldBe "Test Bank"
            bank.isCA shouldBe true // 1 converted to true
            bank.accounts shouldBe emptyList()

            awaitComplete()
        }
    }

    @Test
    fun `should handle empty banks list`() = runTest {
        // Given
        val banksDto = emptyList<BankDto>()
        coEvery { apiService.getBanks() } returns banksDto

        // When & Then
        repository.getBanks().test {
            val result = awaitItem()

            result.isSuccess shouldBe true
            result.getOrNull()?.size shouldBe 0

            awaitComplete()
        }
    }
}