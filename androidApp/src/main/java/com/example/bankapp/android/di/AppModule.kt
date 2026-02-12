package com.example.bankapp.android.di

import com.example.bankapp.android.ui.viewmodels.accounts.AccountsViewModel
import com.example.bankapp.android.ui.viewmodels.operations.OperationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {

    viewModel { AccountsViewModel(get()) }
    viewModel { OperationsViewModel(get()) }
}