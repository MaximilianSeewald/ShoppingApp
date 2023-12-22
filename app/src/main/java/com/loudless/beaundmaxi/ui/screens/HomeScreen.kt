package com.loudless.beaundmaxi.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.loudless.beaundmaxi.ui.elements.BottomBar
import com.loudless.beaundmaxi.ui.elements.TopBarHome

@Composable
fun HomeScreen(navController: NavHostController){
    Scaffold(
        topBar = {
            TopBarHome(
                title = "Home")
        },
        bottomBar = { BottomBar(navController) }) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(
                    top = 10.dp + padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
        ) {

        }
    }
}