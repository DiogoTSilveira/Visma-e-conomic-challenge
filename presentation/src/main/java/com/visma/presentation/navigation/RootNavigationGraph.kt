package com.visma.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.visma.presentation.screen.create.RegisterReceiptScreen
import com.visma.presentation.screen.list.ReceiptListScreen

@Composable
fun RootNavigationGraph(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ReceiptList
    ) {
        composable<ReceiptList> {
            ReceiptListScreen(
                onNavigateToAddReceipt = {
                    navController.navigate(CreateReceipt)
                }
            )
        }

        composable<CreateReceipt> { backStackEntry ->
            //val receipt: Receipt = backStackEntry.toRoute()
            RegisterReceiptScreen(
                onClose = { navController.popBackStack() }
            )
        }
    }
}
