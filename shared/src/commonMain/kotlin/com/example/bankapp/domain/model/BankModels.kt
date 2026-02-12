package com.example.bankapp.domain.model
import kotlinx.serialization.Serializable

@Serializable
data class Bank(
    val name: String,
    val isCA: Boolean,
    val accounts: List<BankAccount>
)

@Serializable
data class BankAccount(
    val order: Int,
    val id: String,
    val holder: String,
    val role: Int,
    val contractNumber: String,
    val label: String,
    val productCode: String,
    val balance: Double,
    val operations: List<Operation>
)

@Serializable
data class Operation(
    val id: String,
    val title: String,
    val amount: Double,
    val category: String,
    val date: Long
)