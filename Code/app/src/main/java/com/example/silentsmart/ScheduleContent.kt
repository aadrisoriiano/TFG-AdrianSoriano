package com.example.silentsmart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder

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
import com.example.silentsmart.ui.theme.WdxFontFamily

@Composable
fun ScheduleContent(
    viewModel: MainViewModel,
    editMode: Boolean = false,
    onHorarioSelected: ((Horario) -> Unit)? = null
) {
    val horarios = viewModel.horarios.collectAsState(initial = emptyList()).value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(horarios.orEmpty().filterNotNull(), key = { it.id }) { horario ->
            ScheduleCard(
                horario = horario,
                viewModel = viewModel,
                editMode = editMode,
                onSelect = if (editMode && onHorarioSelected != null) {
                    { onHorarioSelected(horario) }
                } else null
            )
        }
    }
}

@Composable
fun ScheduleCard(
    horario: Horario,
    viewModel: MainViewModel,
    editMode: Boolean = false,
    onSelect: (() -> Unit)? = null
) {
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
                        fontFamily = WdxFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${horario.horaInicio} - ${horario.horaFin}",
                        fontFamily = WdxFontFamily,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                // --- NUEVO: Checkbox a la izquierda del icono de modo ---
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (editMode && onSelect != null) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = { onSelect() },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
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
                    // --- Icono de favorito (Compose) ---
                    IconButton(
                        onClick = { viewModel.toggleHorarioFavorito(horario) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (horario.favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (horario.favorito) "Quitar de favoritos" else "Marcar como favorito",
                            tint = if (horario.favorito) Color.Red else Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = horario.activado,
                        onCheckedChange = { checked -> viewModel.setHorarioActivado(horario, checked) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF98FF98),
                            checkedTrackColor = Color(0xFF77DD77),
                            uncheckedThumbColor = Color(0xFF666666),
                            uncheckedTrackColor = Color(0xFF999999)
                        )
                    )
                }
            }
        }
    }
}
