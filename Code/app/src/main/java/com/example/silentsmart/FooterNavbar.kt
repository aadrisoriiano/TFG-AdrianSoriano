package com.example.silentsmart

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.silentsmart.ui.theme.SilentSmartTheme


@Composable
fun FooterNavBar() {
    NavigationBar( windowInsets = NavigationBarDefaults.windowInsets ){
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon( Icons.Filled.Notifications , null) },
            label = { Text(text = "Timer") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon( Icons.Filled.DateRange, null) },
            label = { Text(text = "Schedule")}
        )
    }
}

