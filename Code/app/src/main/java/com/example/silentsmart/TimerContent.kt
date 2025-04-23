package com.example.silentsmart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentsmart.database.entity.Temporizador
import com.example.silentsmart.ui.theme.SilentSmartTheme
import androidx.compose.foundation.lazy.items


@Composable
fun TimerContent(viewModel: MainViewModel) {
    val temporizadores = viewModel.temporizadores.collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Primera fila: Temporizador grande
        TimerSection()

        // Segunda fila: Carrusel de temporizadores
      //  TimerCarousel(temporizadores)

        // Tercera fila: Carrusel de horarios
        ScheduleCarousel()
    }
}
@Composable
fun TimerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray) // Color de fondo gris
            .padding(16.dp), // Padding interno
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Temporizador
            Text(
                text = "00:00:00", // Aquí se mostrará el tiempo restante
                fontSize = 32.sp,
                color = Color.Black
            )

            // Botones de control
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Acción para parar */ }) {
                    Icon(Icons.Default.Clear, contentDescription = "Parar")
                }
                IconButton(onClick = { /* Acción para reiniciar */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Reiniciar")
                }
                IconButton(onClick = { /* Acción para continuar */ }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Continuar")
                }
            }
        }
    }
}
@Composable
fun TimerCarousel(temporizadores: List<Temporizador>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(temporizadores, key = { it.id }) { temporizador ->
            TimerCard(temporizador)
        }
    }
}

@Composable
fun TimerCard(temporizador: Temporizador) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray),
        elevation = CardDefaults.cardElevation(4.dp) // Corrección de elevación
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${temporizador.horas}h ${temporizador.minutos}m",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* Acción para iniciar temporizador */ }) {
                Text("Iniciar")
            }
        }
    }
}
@Composable
fun ScheduleCarousel() {
    // Carrusel de horarios
    Text("Carrusel de Horarios (Placeholder)")
}

@Preview(showBackground = true)
@Composable
fun TimerContentPreview() {
    SilentSmartTheme {
        Column(

        ) {
       // TimerContent()
        }
    }
}