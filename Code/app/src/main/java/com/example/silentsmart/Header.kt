package com.example.silentsmart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentsmart.ui.theme.SilentSmartTheme

@Composable
fun Header(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopButtons()
        Title(title)
        ThreeColumnButtons()
    }
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
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = "Edit", color = Color.Black)
        }

        Button(
            onClick = { /* Acción del botón + */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = "+", color = Color.Black)
        }
    }
}

@Composable
fun Title(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 15.dp),
        color = Color.Black,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,

    )
}

@Composable
fun ThreeColumnButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { /* Acción para silenciar el móvil */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = "Silencio", color = Color.Black)
        }

        Button(
            onClick = { /* Acción para poner en vibración */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = "Vibración", color = Color.Black)
        }

        Button(
            onClick = { /* Acción para modo sonido */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = "Sonido", color = Color.Black)
        }
    }
}


@Composable
fun GreetingHeader(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()


    )
}
