package com.example.silentsmart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentsmart.R
import com.example.silentsmart.database.entity.Horario
import com.example.silentsmart.Modo
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ScheduleContent(viewModel: MainViewModel) {
    val horarios = viewModel.horarios.collectAsState(initial = emptyList()).value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(horarios.orEmpty().filterNotNull(), key = { it.id }) { horario ->
            ScheduleCard(horario)
        }
    }
}

@Composable
fun ScheduleCard(horario: Horario) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp))
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = horario.diaSemana,
                        fontSize = 22.sp, // Más grande
                        fontWeight = FontWeight.Bold, // En negrita
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${horario.horaInicio} - ${horario.horaFin}",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val iconRes = when (horario.modo) {
                        Modo.SILENCIO -> R.drawable.volume_off
                        Modo.VIBRACION -> R.drawable.vibration
                        Modo.SONIDO -> R.drawable.volume_up
                        else -> R.drawable.volume_off
                    }
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Modo: ${horario.modo}",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Switch(
                        checked = horario.activado,
                        onCheckedChange = { /* Cambiar activado */ },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF4CAF50),
                            checkedTrackColor = Color.Green
                        )
                    )
                }
            }
        }
    }
}
