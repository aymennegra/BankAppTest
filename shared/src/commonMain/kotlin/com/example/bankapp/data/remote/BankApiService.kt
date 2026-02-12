package com.example.bankapp.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class BankApiService(
    private val httpClient: HttpClient
) {
    companion object {
        private const val BASE_URL = "https://cdf-test-mobile-default-rtdb.europe-west1.firebasedatabase.app"
    }

    suspend fun getBanks(): List<BankDto> {
        return httpClient.get("$BASE_URL/banks.json").body()
    }
}