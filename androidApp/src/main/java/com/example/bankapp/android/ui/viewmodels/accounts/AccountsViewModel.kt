package com.example.bankapp.android.ui.viewmodels.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.domain.model.Bank
import com.example.bankapp.domain.usecase.GetBanksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val getBanksUseCase: GetBanksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Loading)
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    init {
        loadBanks()
    }

    fun loadBanks() {
        viewModelScope.launch {
            _uiState.value = AccountsUiState.Loading

            getBanksUseCase.getBanks().collect { result ->

                if (result.isSuccess) {
                    val banks = result.getOrNull() ?: emptyList()
                    val groupedBanks = groupBanksByType(banks)
                    _uiState.value = AccountsUiState.Success(groupedBanks)

                } else {
                    val error = result.exceptionOrNull()
                    _uiState.value = AccountsUiState.Error(
                        error?.message ?: "Une erreur est survenue"
                    )
                }
            }
        }
    }


    private fun groupBanksByType(banks: List<Bank>): GroupedBanks {
        val caBanks = banks.filter { it.isCA }.sortedBy { it.name }
        val otherBanks = banks.filter { !it.isCA }.sortedBy { it.name }

        return GroupedBanks(
            creditAgricoleBanks = caBanks,
            otherBanks = otherBanks
        )
    }

    fun toggleBankExpansion(bankName: String) {
        val currentState = _uiState.value
        if (currentState is AccountsUiState.Success) {
            val expandedBanks = currentState.expandedBanks.toMutableSet()
            if (expandedBanks.contains(bankName)) {
                expandedBanks.remove(bankName)
            } else {
                expandedBanks.add(bankName)
            }
            _uiState.value = currentState.copy(expandedBanks = expandedBanks)
        }
    }

    fun isBankExpanded(bankName: String): Boolean {
        val currentState = _uiState.value
        return if (currentState is AccountsUiState.Success) {
            currentState.expandedBanks.contains(bankName)
        } else {
            false
        }
    }
}

sealed class AccountsUiState {
    object Loading : AccountsUiState()
    data class Success(
        val groupedBanks: GroupedBanks,
        val expandedBanks: Set<String> = emptySet()
    ) : AccountsUiState()
    data class Error(val message: String) : AccountsUiState()
}

data class GroupedBanks(
    val creditAgricoleBanks: List<Bank>,
    val otherBanks: List<Bank>
)