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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import com.example.silentsmart.ui.theme.SilentSmartTheme
import com.example.silentsmart.ScheduleContent
import com.example.silentsmart.TimerContent

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilentSmartTheme {
                val view = LocalView.current
                val window = (view.context as ComponentActivity).window
                SideEffect {
                    // Fondo blanco en barra de navegación y notificaciones
                    window.statusBarColor = Color.White.toArgb()
                    window.navigationBarColor = Color.White.toArgb()


                }

                var selectedTab by remember { mutableStateOf("Timer") }
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
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
                            .navigationBarsPadding()

                    ) {
                        Header(title = if (selectedTab == "Timer") "Timer" else "Schedule", viewModel = mainViewModel)
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
