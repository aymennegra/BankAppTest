package com.example.bankapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BankDto(
    val name: String,
    val isCA: Int,
    val accounts: List<BankAccountDto>
)

@Serializable
data class BankAccountDto(
    val order: Int,
    val id: String,
    val holder: String,
    val role: Int,
    @SerialName("contract_number")
    val contractNumber: String,
    val label: String,
    @SerialName("product_code")
    val productCode: String,
    val balance: Double,
    val operations: List<OperationDto>
)

@Serializable
data class OperationDto(
    val id: String,
    val title: String,
    val amount: String,
    val category: String,
    val date: String
)