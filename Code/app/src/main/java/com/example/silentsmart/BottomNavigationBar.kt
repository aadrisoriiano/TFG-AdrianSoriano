package com.example.silentsmart

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    BottomAppBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = selectedTab == "Timer",
            onClick = { onTabSelected("Timer") },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Timer") },
            label = { Text("Timer") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.DarkGray,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.DarkGray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.LightGray // Fondo del indicador de selección
            )
        )
        NavigationBarItem(
            selected = selectedTab == "Schedule",
            onClick = { onTabSelected("Schedule") },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Schedule") },
            label = { Text("Schedule") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.DarkGray,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.DarkGray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.LightGray
            )
        )
    }
}
