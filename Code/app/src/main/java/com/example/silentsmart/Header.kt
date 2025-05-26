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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.silentsmart.ui.theme.SilentSmartTheme
import com.example.silentsmart.R

import androidx.compose.ui.platform.LocalContext

@Composable
fun Header(title: String, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopButtons()
        Title(title)
        ThreeColumnButtons(viewModel)
    }
}
@Composable
fun TopButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, start = 6.dp, end = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Botón Edit
        Box(
            modifier = Modifier
                .height(26.dp)
                .width(70.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { /* Acción del botón Edit */ },
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Edit",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        // Botón +
        Box(
            modifier = Modifier
                .height(26.dp)
                .width(70.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(Color.LightGray),

            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { /* Acción del botón + */ },
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "+",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
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
        style = LocalTextStyle.current.copy(
            shadow = Shadow(
                color = Color(0xFF570606),
                offset = Offset(2f, 2f),
                blurRadius = 5f
            )
        )
    )
}

@Composable
fun ThreeColumnButtons(viewModel: MainViewModel) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(30.dp))
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
                onClick = { viewModel.setAudioMode(context, Modo.SILENCIO) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.volume_off),
                    contentDescription = "Silencio",
                    tint = Color.Black
                )
            }
            IconButton(
                onClick = { viewModel.setAudioMode(context, Modo.VIBRACION) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.vibration),
                    contentDescription = "Vibración",
                    tint = Color.Black
                )
            }
            IconButton(
                onClick = { viewModel.setAudioMode(context, Modo.SONIDO) }
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
