package com.example.bankapp.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.bankapp.android.ui.screens.AccountsScreen
import com.example.bankapp.android.ui.screens.OperationsScreen

@Composable
fun BankAppNavigation(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = "accounts"
    ) {

        composable("accounts") {
            AccountsScreen(
                onAccountClick = { account ->
                    navController.navigate("operations/${account.id}")
                }
            )
        }

        composable("operations/{accountId}") { backStackEntry ->

            val accountId = backStackEntry.arguments
                ?.getString("accountId")
                .orEmpty()

            OperationsScreen(
                accountId = accountId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}