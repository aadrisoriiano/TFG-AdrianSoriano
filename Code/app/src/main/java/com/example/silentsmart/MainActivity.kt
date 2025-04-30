package com.example.silentsmart


import BottomNavigationBarWithDivider
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import com.example.silentsmart.ui.theme.SilentSmartTheme
import com.example.silentsmart.ScheduleContent
import com.example.silentsmart.TimerContent

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilentSmartTheme {
                var selectedTab by remember { mutableStateOf("Timer") }
                Scaffold(
                    bottomBar = {
                        BottomNavigationBarWithDivider(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color.White)

                    ) {
                        Header(title = if (selectedTab == "Timer") "Timer" else "Schedule")
                        Spacer(modifier = Modifier.height(2.dp))
                        when (selectedTab) {
                            "Timer" -> TimerContent(viewModel = mainViewModel)
                            "Schedule" -> ScheduleContent(viewModel = mainViewModel)
                        }
                    }
                }
            }
        }
    }
}
