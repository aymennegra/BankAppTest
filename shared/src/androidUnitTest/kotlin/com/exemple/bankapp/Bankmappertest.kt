package com.exemple.bankapp

import com.example.bankapp.data.mapper.toDomain
import com.example.bankapp.data.remote.BankAccountDto
import com.example.bankapp.data.remote.BankDto
import com.example.bankapp.data.remote.OperationDto
import kotlin.test.Test
import kotlin.test.assertEquals

class BankMapperTest {

    @Test
    fun `should convert BankDto to Domain Bank correctly`() {
        val bankDto = BankDto(
            name = "CA Languedoc",
            isCA = 1,
            accounts = emptyList()
        )

        val bank = bankDto.toDomain()

        assertEquals("CA Languedoc", bank.name)
        assertEquals(true, bank.isCA)
        assertEquals(0, bank.accounts.size)
    }

    @Test
    fun `should convert isCA from 1 to true`() {
        val bankDto = BankDto(
            name = "Test Bank",
            isCA = 1,
            accounts = emptyList()
        )

        val bank = bankDto.toDomain()

        assertEquals(true, bank.isCA)
    }

    @Test
    fun `should convert isCA from 0 to false`() {
        val bankDto = BankDto(
            name = "Test Bank",
            isCA = 0,
            accounts = emptyList()
        )

        val bank = bankDto.toDomain()

        assertEquals(false, bank.isCA)
    }

    @Test
    fun `should convert BankAccountDto to Domain BankAccount correctly`() {
        val accountDto = BankAccountDto(
            order = 1,
            id = "123456",
            holder = "John Doe",
            role = 1,
            contractNumber = "CONTRACT123",
            label = "Compte Courant",
            productCode = "00001",
            balance = 1500.50,
            operations = emptyList()
        )

        val account = accountDto.toDomain()

        assertEquals("123456", account.id)
        assertEquals("John Doe", account.holder)
        assertEquals("Compte Courant", account.label)
        assertEquals(1500.50, account.balance, 0.001)
        assertEquals("CONTRACT123", account.contractNumber)
    }

    @Test
    fun `should convert OperationDto amount from string with comma to double`() {
        val operationDto = OperationDto(
            id = "1",
            title = "Test Operation",
            amount = "-15,99",
            category = "leisure",
            date = "1644870724"
        )

        val operation = operationDto.toDomain()

        assertEquals(-15.99, operation.amount, 0.001)
    }

    @Test
    fun `should convert OperationDto amount from string with dot to double`() {
        val operationDto = OperationDto(
            id = "1",
            title = "Test Operation",
            amount = "-15.99",
            category = "leisure",
            date = "1644870724"
        )

        val operation = operationDto.toDomain()

        assertEquals(-15.99, operation.amount, 0.001)
    }

    @Test
    fun `should convert OperationDto date from string to long`() {
        val operationDto = OperationDto(
            id = "1",
            title = "Test Operation",
            amount = "-15.99",
            category = "leisure",
            date = "1644870724"
        )

        val operation = operationDto.toDomain()

        assertEquals(1644870724L, operation.date)
    }

    @Test
    fun `should handle invalid amount and return 0`() {
        val operationDto = OperationDto(
            id = "1",
            title = "Test Operation",
            amount = "invalid",
            category = "leisure",
            date = "1644870724"
        )

        val operation = operationDto.toDomain()

        assertEquals(0.0, operation.amount, 0.001)
    }

    @Test
    fun `should handle invalid date and return 0`() {
        val operationDto = OperationDto(
            id = "1",
            title = "Test Operation",
            amount = "-15.99",
            category = "leisure",
            date = "invalid"
        )

        val operation = operationDto.toDomain()

        assertEquals(0L, operation.date)
    }

    @Test
    fun `should convert complete bank hierarchy correctly`() {
        val operationDto = OperationDto(
            id = "1",
            title = "Netflix",
            amount = "-15,99",
            category = "leisure",
            date = "1644870724"
        )

        val accountDto = BankAccountDto(
            order = 1,
            id = "123",
            holder = "John Doe",
            role = 1,
            contractNumber = "CONTRACT",
            label = "Compte",
            productCode = "00001",
            balance = 1000.0,
            operations = listOf(operationDto)
        )

        val bankDto = BankDto(
            name = "CA Test",
            isCA = 1,
            accounts = listOf(accountDto)
        )

        val bank = bankDto.toDomain()

        assertEquals("CA Test", bank.name)
        assertEquals(true, bank.isCA)
        assertEquals(1, bank.accounts.size)
        assertEquals(1, bank.accounts[0].operations.size)
        assertEquals("Netflix", bank.accounts[0].operations[0].title)
        assertEquals(-15.99, bank.accounts[0].operations[0].amount, 0.001)
    }
}
