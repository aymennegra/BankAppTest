package com.example.bankapp.android.ui.viewmodels.operations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.domain.model.BankAccount
import com.example.bankapp.domain.model.Operation
import com.example.bankapp.domain.usecase.GetBanksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OperationsViewModel(
    private val getBanksUseCase: GetBanksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OperationsUiState>(OperationsUiState.Loading)
    val uiState: StateFlow<OperationsUiState> = _uiState.asStateFlow()

    fun loadOperations(accountId: String) {
        viewModelScope.launch {
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
                    val error = result.exceptionOrNull()
                    _uiState.value = OperationsUiState.Error(
                        error?.message ?: "Une erreur est survenue"
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