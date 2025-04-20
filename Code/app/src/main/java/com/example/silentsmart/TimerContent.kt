package com.example.silentsmart

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Primera fila: Temporizador grande
        TimerSection()

        // Segunda fila: Carrusel de temporizadores
        TimerCarousel()

        // Tercera fila: Carrusel de horarios
        ScheduleCarousel()
    }
}

@Composable
fun TimerSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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

@Composable
fun TimerCarousel() {
    // Carrusel de temporizadores
    Text("Carrusel de Temporizadores (Placeholder)")
}

@Composable
fun ScheduleCarousel() {
    // Carrusel de horarios
    Text("Carrusel de Horarios (Placeholder)")
}