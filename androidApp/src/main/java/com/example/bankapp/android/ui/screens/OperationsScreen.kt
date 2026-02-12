package com.example.bankapp.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankapp.android.ui.viewmodels.operations.OperationsUiState
import com.example.bankapp.android.ui.viewmodels.operations.OperationsViewModel
import com.example.bankapp.domain.model.Operation
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsScreen(
    accountId: String,
    viewModel: OperationsViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(accountId) {
        viewModel.loadOperations(accountId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        when (val state = uiState) {
            is OperationsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is OperationsUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "%.2f €".format(state.account.balance),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = state.account.label,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    if (state.operations.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aucune opération",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.operations) { operation ->
                                OperationItem(operation = operation)
                            }
                        }
                    }
                }
            }
            is OperationsUiState.Error -> {
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
                        Button(onClick = { viewModel.loadOperations(accountId) }) {
                            Text("Réessayer")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OperationItem(operation: Operation) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = operation.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Text(
                        text = formatDate(operation.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E),
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = "%.2f €".format(operation.amount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF9E9E9E)
                )
            }
        }
        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 0.5.dp
        )
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    return sdf.format(Date(timestamp * 1000))
}