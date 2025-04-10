package com.visma.presentation.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.visma.presentation.screen.create.RegisterReceiptScreen
import com.visma.presentation.screen.list.ReceiptListScreen

@Composable
fun RootNavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.systemBarsPadding(),
        navController = navController,
        startDestination = ReceiptList
    ) {
        composable<ReceiptList> {
            ReceiptListScreen(
                onNavigateToAddReceipt = {
                    navController.navigate(RegisterReceipt())
                },
                onNavigateToEditReceipt = { id ->
                    navController.navigate(RegisterReceipt(id))
                }
            )
        }

        composable<RegisterReceipt> {
            RegisterReceiptScreen(
                onClose = { navController.popBackStack() }
            )
        }
    }
}
