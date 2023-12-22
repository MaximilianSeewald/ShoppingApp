package com.loudless.beaundmaxi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.loudless.beaundmaxi.logic.DataRepository
import com.loudless.beaundmaxi.logic.DatabaseManager
import com.loudless.beaundmaxi.ui.screens.ShoppingScreen
import com.loudless.beaundmaxi.ui.theme.BeaUndMaxiTheme
import com.loudless.beaundmaxi.logic.viewModel.ShoppingViewModel
import com.loudless.beaundmaxi.ui.NavGraphCustom

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databaseManager = DatabaseManager()
        val dataRepository = DataRepository(databaseManager)
        dataRepository.init()
        setContent {
            BeaUndMaxiTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraphCustom(navController = navController, dataRepository = dataRepository)
                }
            }
        }
    }
}