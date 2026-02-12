package com.example.bankapp.data.mapper

import com.example.bankapp.data.remote.BankAccountDto
import com.example.bankapp.data.remote.BankDto
import com.example.bankapp.data.remote.OperationDto
import com.example.bankapp.domain.model.Bank
import com.example.bankapp.domain.model.BankAccount
import com.example.bankapp.domain.model.Operation

fun BankDto.toDomain(): Bank {
    return Bank(
        name = name,
        isCA = isCA == 1,
        accounts = accounts.map { it.toDomain() }
    )
}

fun BankAccountDto.toDomain(): BankAccount {
    return BankAccount(
        order = order,
        id = id,
        holder = holder,
        role = role,
        contractNumber = contractNumber,
        label = label,
        productCode = productCode,
        balance = balance,
        operations = operations.map { it.toDomain() }
    )
}

fun OperationDto.toDomain(): Operation {
    // Convertir le montant de String à Double
    val amountValue = amount.replace(",", ".").toDoubleOrNull() ?: 0.0

    // Convertir la date de String timestamp à Long
    val dateValue = date.toLongOrNull() ?: 0L

    return Operation(
        id = id,
        title = title,
        amount = amountValue,
        category = category,
        date = dateValue
    )
}
