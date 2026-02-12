package com.example.bankapp.di

import com.example.bankapp.HttpClientFactory
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

}
