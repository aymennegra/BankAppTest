package com.example.bankapp.di

import com.example.bankapp.HttpClientFactory
import com.example.bankapp.presentation.viewmodels.accounts.AccountsViewModel
import com.example.bankapp.presentation.viewmodels.operations.OperationsViewModel
import com.example.bankapp.data.remote.BankApiService
import com.example.bankapp.data.repository.BankRepositoryImpl
import com.example.bankapp.domain.repository.BankRepository
import com.example.bankapp.domain.usecase.GetBanksUseCase
import org.koin.dsl.module

val SharedModule = module {

    single { HttpClientFactory.create() }
    single { BankApiService(get()) }

    single<BankRepository> { BankRepositoryImpl(get()) }

    single { GetBanksUseCase(get()) }

    single{ AccountsViewModel(get()) }
    single{ OperationsViewModel(get()) }

}
