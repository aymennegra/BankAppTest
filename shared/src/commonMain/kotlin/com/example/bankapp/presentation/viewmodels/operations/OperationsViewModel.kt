package com.example.bankapp.presentation.viewmodels.operations

import com.example.bankapp.domain.model.BankAccount
import com.example.bankapp.domain.model.Operation
import com.example.bankapp.domain.usecase.GetBanksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OperationsViewModel(
    private val getBanksUseCase: GetBanksUseCase
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _uiState = MutableStateFlow<OperationsUiState>(OperationsUiState.Loading)
    val uiState: StateFlow<OperationsUiState> = _uiState.asStateFlow()

    fun loadOperations(accountId: String) {
        scope.launch {
            _uiState.value = OperationsUiState.Loading

            getBanksUseCase.getBanks().collect { result ->
                if (result.isSuccess) {
                    val banks = result.getOrNull() ?: emptyList()

                    val account = banks
                        .flatMap { it.accounts }
                        .firstOrNull { it.id == accountId }

                    if (account != null) {
                        val sortedOperations = account.operations.sortedWith(
                            compareByDescending<Operation> { it.date }
                                .thenBy { it.title }
                        )

                        _uiState.value = OperationsUiState.Success(
                            account = account,
                            operations = sortedOperations
                        )
                    } else {
                        _uiState.value = OperationsUiState.Error("Compte non trouvé")
                    }
                } else {
                    _uiState.value = OperationsUiState.Error(
                        result.exceptionOrNull()?.message ?: "Une erreur est survenue"
                    )
                }
            }
        }
    }
}

sealed class OperationsUiState {
    object Loading : OperationsUiState()
    data class Success(
        val account: BankAccount,
        val operations: List<Operation>
    ) : OperationsUiState()
    data class Error(val message: String) : OperationsUiState()
}
