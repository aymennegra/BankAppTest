package com.example.bankapp.data.repository

import com.example.bankapp.data.mapper.toDomain
import com.example.bankapp.data.remote.BankApiService
import com.example.bankapp.domain.model.Bank
import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BankRepositoryImpl(
    private val apiService: BankApiService
) : BankRepository {

    override fun getBanks(): Flow<Result<List<Bank>>> = flow {
        try {
            val response = apiService.getBanks() // List<BankDto>
            val banks = response.map { it.toDomain() } // map directly
            emit(Result.success(banks))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}