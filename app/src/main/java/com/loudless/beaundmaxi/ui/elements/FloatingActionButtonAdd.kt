package com.loudless.beaundmaxi.ui.elements

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionButtonAdd(onClick: () -> Unit){
    FloatingActionButton(onClick = onClick, modifier = Modifier.size(69.dp)) {
        Icon(Icons.Filled.Add, "Add", modifier = Modifier.size(32.dp))
    }
}
