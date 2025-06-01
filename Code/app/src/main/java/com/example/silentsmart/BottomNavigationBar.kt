package com.example.silentsmart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentsmart.ui.theme.WdxFontFamily // <-- Importa tu font

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    // Floating effect y sombra
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        shape = RoundedCornerShape(20.dp), // Igual que TimerSection
        color = Color(0xFFF2F2F2),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBarItem(
                selected = selectedTab == "Temporizador",
                onClick = { onTabSelected("Temporizador") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.timer_icon),
                        contentDescription = "Temporizador",
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = {
                    Text(
                        "Temporizador",
                        fontSize = 14.sp,
                        fontWeight = if (selectedTab == "Temporizador") FontWeight.Bold else FontWeight.Normal,
                        fontFamily = WdxFontFamily // 
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF222222),
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFF222222),
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFFF2F2F2)
                )
            )
            NavigationBarItem(
                selected = selectedTab == "Horario",
                onClick = { onTabSelected("Horario") },
                icon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Horario",
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = {
                    Text(
                        "Horario",
                        fontSize = 14.sp,
                        fontWeight = if (selectedTab == "Horario") FontWeight.Bold else FontWeight.Normal,
                        fontFamily = WdxFontFamily
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF222222),
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFF222222),
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFFF2F2F2)
                )
            )
        }
    }
}
