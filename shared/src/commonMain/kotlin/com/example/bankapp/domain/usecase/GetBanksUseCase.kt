package com.example.bankapp.domain.usecase

import com.example.bankapp.domain.model.Bank
import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow

class GetBanksUseCase(
    private val repository: BankRepository
) {
    fun getBanks(): Flow<Result<List<Bank>>> {
        return repository.getBanks()
    }
}