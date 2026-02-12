package com.example.bankapp.domain.repository

import com.example.bankapp.domain.model.Bank
import kotlinx.coroutines.flow.Flow

interface BankRepository {
    fun getBanks(): Flow<Result<List<Bank>>>
}