package com.loudless.beaundmaxi.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BottomBar(navController: NavHostController) {
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null)
                }
                IconButton(onClick = { navController.navigate("shopping") }) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                }
                IconButton(onClick = { navController.navigate("recipe") }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
        tonalElevation = 16.dp)
}