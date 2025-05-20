package com.example.silentsmart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.silentsmart.ui.theme.SilentSmartTheme
import com.example.silentsmart.R

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
            Text(
                text = "Edit",
                color = Color.Black,
                fontSize = 24.sp,
               
            )
        }

        Button(
            onClick = { /* Acción del botón + */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "+",
                color = Color.Black,
                fontSize = 28.sp,
             
            )
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(30.dp)) // Sombra y mismo radio que el clip
            .clip(RoundedCornerShape(30.dp))
            .background(Color.LightGray)
            .padding(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Acción para silenciar el móvil */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.volume_off),
                    contentDescription = "Silencio",
                    tint = Color.Black
                )
            }
            IconButton(
                onClick = { /* Acción para poner en vibración */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.vibration),
                    contentDescription = "Vibración",
                    tint = Color.Black
                )
            }
            IconButton(
                onClick = { /* Acción para modo sonido */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.volume_up),
                    contentDescription = "Sonido",
                    tint = Color.Black
                )
            }
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
