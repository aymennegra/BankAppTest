package com.example.bankapp.presentation.viewmodels.accounts

import com.example.bankapp.domain.model.Bank
import com.example.bankapp.domain.usecase.GetBanksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val getBanksUseCase: GetBanksUseCase
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _uiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Loading)
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    init {
        loadBanks()
    }

    fun loadBanks() {
        scope.launch {
            _uiState.value = AccountsUiState.Loading
            getBanksUseCase.getBanks().collect { result ->
                if (result.isSuccess) {
                    val banks = result.getOrNull() ?: emptyList()
                    val groupedBanks = groupBanksByType(banks)
                    _uiState.value = AccountsUiState.Success(groupedBanks)
                } else {
                    _uiState.value = AccountsUiState.Error(
                        result.exceptionOrNull()?.message ?: "Une erreur est survenue"
                    )
                }
            }
        }
    }

    private fun groupBanksByType(banks: List<Bank>): GroupedBanks {
        val caBanks = banks.filter { it.isCA }.sortedBy { it.name }
        val otherBanks = banks.filter { !it.isCA }.sortedBy { it.name }
        return GroupedBanks(creditAgricoleBanks = caBanks, otherBanks = otherBanks)
    }

    fun toggleBankExpansion(bankName: String) {
        val currentState = _uiState.value
        if (currentState is AccountsUiState.Success) {
            val expandedBanks = currentState.expandedBanks.toMutableSet()
            if (!expandedBanks.add(bankName)) expandedBanks.remove(bankName)
            _uiState.value = currentState.copy(expandedBanks = expandedBanks)
        }
    }

    fun isBankExpanded(bankName: String): Boolean {
        val currentState = _uiState.value
        return currentState is AccountsUiState.Success && currentState.expandedBanks.contains(bankName)
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
