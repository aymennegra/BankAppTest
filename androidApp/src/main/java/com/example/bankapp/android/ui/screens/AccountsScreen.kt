package com.example.bankapp.android.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankapp.presentation.viewmodels.accounts.AccountsUiState
import com.example.bankapp.presentation.viewmodels.accounts.AccountsViewModel
import com.example.bankapp.domain.model.Bank
import com.example.bankapp.domain.model.BankAccount
import org.koin.androidx.compose.get


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel = get(),
    onAccountClick: (BankAccount) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mes Comptes",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        when (val state = uiState) {
            is AccountsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AccountsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // RG02: Première section: Les comptes Crédit Agricole
                    if (state.groupedBanks.creditAgricoleBanks.isNotEmpty()) {
                        item {
                            SectionHeader(title = "Crédit Agricole")
                        }
                        items(state.groupedBanks.creditAgricoleBanks) { bank ->
                            BankItem(
                                bank = bank,
                                isExpanded = viewModel.isBankExpanded(bank.name),
                                onBankClick = { viewModel.toggleBankExpansion(bank.name) },
                                onAccountClick = onAccountClick
                            )
                        }
                    }

                    // RG02: Deuxième section: Les autres banques
                    if (state.groupedBanks.otherBanks.isNotEmpty()) {
                        item {
                            SectionHeader(title = "Autres Banques")
                        }
                        items(state.groupedBanks.otherBanks) { bank ->
                            BankItem(
                                bank = bank,
                                isExpanded = viewModel.isBankExpanded(bank.name),
                                onBankClick = { viewModel.toggleBankExpansion(bank.name) },
                                onAccountClick = onAccountClick
                            )
                        }
                    }
                }
            }
            is AccountsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadBanks() }) {
                            Text("Réessayer")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Normal,
        color = Color(0xFF9E9E9E),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun BankItem(
    bank: Bank,
    isExpanded: Boolean,
    onBankClick: () -> Unit,
    onAccountClick: (BankAccount) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp)
    ) {
        // RG01: La cellule « BankAccount » est dépliante (collapsible)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onBankClick),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bank.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "%.2f €".format(bank.accounts.sumOf { it.balance }),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF9E9E9E)
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Réduire" else "Développer",
                        tint = Color(0xFF9E9E9E)
                    )
                }
            }
        }

        // RG04: Lorsqu'on déplie une banque, affichez la liste des comptes
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                bank.accounts.sortedBy { it.order }.forEach { account ->
                    AccountListItem(
                        account = account,
                        onClick = { onAccountClick(account) }
                    )
                }
            }
        }

        // Divider entre les items
        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 0.5.dp
        )
    }
}

@Composable
fun AccountListItem(
    account: BankAccount,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "%.2f €".format(account.balance),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF9E9E9E)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}