package com.example.silentsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.silentsmart.ui.theme.SilentSmartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilentSmartTheme {
             Scaffold(
                 modifier = Modifier.fillMaxSize(),
                 topBar = { TopButtons() }
             ) { innerPadding ->
                 Greeting(
                     name = "Android",
                     modifier = Modifier.padding(innerPadding)
                 )
             }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
        

    )
}

@Composable
fun TopButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { /* Acción del botón Edit */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray), // Naranja
            shape = RoundedCornerShape(20.dp) // Bordes redondeados
        ) {
            Text(text = "Edit", color = Color.Black)
        }

        Button(
            onClick = { /* Acción del botón + */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray), // Naranja
            shape = RoundedCornerShape(20.dp) // Bordes redondeados
        ) {
            Text(text = "+", color = Color.Black)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SilentSmartTheme {
        Greeting("Android")
        TopButtons()
    }
}