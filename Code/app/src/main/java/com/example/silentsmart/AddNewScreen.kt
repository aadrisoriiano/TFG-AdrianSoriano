package com.example.silentsmart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.silentsmart.ui.theme.WdxFontFamily

@Composable
fun AddEditScreen(
    isEdit: Boolean = false,
    initialType: String? = null, // "timer" o "schedule"
    initialModo: Modo? = null,
    initialHours: Int? = null,
    initialMinutes: Int? = null,
    initialDay: String? = null,
    initialStartHour: String? = null,
    initialEndHour: String? = null,
    onSave: (
        isTimer: Boolean,
        modo: Modo,
        hours: Int?,
        minutes: Int?,
        day: String?,
        startHour: String?,
        endHour: String?
    ) -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)? = null // Solo para editar
) {
    var selectedType by remember { mutableStateOf(initialType) }
    var selectedModo by remember { mutableStateOf(initialModo) }
    var hours by remember { mutableStateOf(initialHours ?: 0) }
    var minutes by remember { mutableStateOf(initialMinutes ?: 5) }
    var dayOfWeek by remember { mutableStateOf(initialDay ?: "Lunes") }
    var startHour by remember { mutableStateOf(initialStartHour?.split(":")?.getOrNull(0) ?: "08") }
    var startMinute by remember { mutableStateOf(initialStartHour?.split(":")?.getOrNull(1) ?: "00") }
    var endHour by remember { mutableStateOf(initialEndHour?.split(":")?.getOrNull(0) ?: "09") }
    var endMinute by remember { mutableStateOf(initialEndHour?.split(":")?.getOrNull(1) ?: "00") }

    // Desplegables
    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val horas = (0..23).map { it.toString().padStart(2, '0') }
    val minutosList = (0..59).map { it.toString().padStart(2, '0') }

    var dayDropdownExpanded by remember { mutableStateOf(false) }
    var startHourDropdownExpanded by remember { mutableStateOf(false) }
    var startMinuteDropdownExpanded by remember { mutableStateOf(false) }
    var endHourDropdownExpanded by remember { mutableStateOf(false) }
    var endMinuteDropdownExpanded by remember { mutableStateOf(false) }
    var timerHourDropdownExpanded by remember { mutableStateOf(false) }
    var timerMinuteDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        Text(
            text = "Nuevo",
            fontFamily = WdxFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Selección Temporizador o Horario
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { selectedType = "timer" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == "timer") Color(0xFFB2FFB2) else Color.White
                    )
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Temporizador")
                    Spacer(Modifier.width(8.dp))
                    Text("Temporizador", fontFamily = WdxFontFamily)
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { selectedType = "schedule" },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == "schedule") Color(0xFFB2FFB2) else Color.White
                    )
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = "Horario")
                    Spacer(Modifier.width(8.dp))
                    Text("Horario", fontFamily = WdxFontFamily)
                }
            }
        }

        // Selección de modo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { selectedModo = Modo.SILENCIO },
                    modifier = Modifier.background(
                        if (selectedModo == Modo.SILENCIO) Color(0xFFB2FFB2) else Color.Transparent,
                        RoundedCornerShape(12.dp)
                    )
                ) {
                    Icon(
                        painterResource(id = R.drawable.volume_off),
                        contentDescription = "Silencio",
                        tint = if (selectedModo == Modo.SILENCIO) Color(0xFF570606) else Color.Black
                    )
                }
                IconButton(
                    onClick = { selectedModo = Modo.VIBRACION },
                    modifier = Modifier.background(
                        if (selectedModo == Modo.VIBRACION) Color(0xFFB2FFB2) else Color.Transparent,
                        RoundedCornerShape(12.dp)
                    )
                ) {
                    Icon(
                        painterResource(id = R.drawable.vibration),
                        contentDescription = "Vibración",
                        tint = if (selectedModo == Modo.VIBRACION) Color(0xFF570606) else Color.Black
                    )
                }
                IconButton(
                    onClick = { selectedModo = Modo.SONIDO },
                    modifier = Modifier.background(
                        if (selectedModo == Modo.SONIDO) Color(0xFFB2FFB2) else Color.Transparent,
                        RoundedCornerShape(12.dp)
                    )
                ) {
                    Icon(
                        painterResource(id = R.drawable.volume_up),
                        contentDescription = "Sonido",
                        tint = if (selectedModo == Modo.SONIDO) Color(0xFF570606) else Color.Black
                    )
                }
            }
        }

        // Opciones según tipo
        if (selectedType == "timer") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Horas:", fontFamily = WdxFontFamily)
                    Spacer(Modifier.width(8.dp))
                    Box {
                        Button(
                            onClick = { timerHourDropdownExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text(hours.toString().padStart(2, '0'), fontFamily = WdxFontFamily)
                        }
                        DropdownMenu(
                            expanded = timerHourDropdownExpanded,
                            onDismissRequest = { timerHourDropdownExpanded = false }
                        ) {
                            horas.forEach { h ->
                                DropdownMenuItem(
                                    text = { Text(h, fontFamily = WdxFontFamily) },
                                    onClick = {
                                        hours = h.toInt()
                                        timerHourDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Text("Minutos:", fontFamily = WdxFontFamily)
                    Spacer(Modifier.width(8.dp))
                    Box {
                        Button(
                            onClick = { timerMinuteDropdownExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text(minutes.toString().padStart(2, '0'), fontFamily = WdxFontFamily)
                        }
                        DropdownMenu(
                            expanded = timerMinuteDropdownExpanded,
                            onDismissRequest = { timerMinuteDropdownExpanded = false }
                        ) {
                            minutosList.forEach { m ->
                                DropdownMenuItem(
                                    text = { Text(m, fontFamily = WdxFontFamily) },
                                    onClick = {
                                        minutes = m.toInt()
                                        timerMinuteDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        } else if (selectedType == "schedule") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    // Día de la semana
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Día:", fontFamily = WdxFontFamily)
                        Spacer(Modifier.width(8.dp))
                        Box {
                            Button(
                                onClick = { dayDropdownExpanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(dayOfWeek, fontFamily = WdxFontFamily)
                            }
                            DropdownMenu(
                                expanded = dayDropdownExpanded,
                                onDismissRequest = { dayDropdownExpanded = false }
                            ) {
                                dias.forEach { dia ->
                                    DropdownMenuItem(
                                        text = { Text(dia, fontFamily = WdxFontFamily) },
                                        onClick = {
                                            dayOfWeek = dia
                                            dayDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    // Hora inicio
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Inicio:", fontFamily = WdxFontFamily)
                        Spacer(Modifier.width(8.dp))
                        Box {
                            Button(
                                onClick = { startHourDropdownExpanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(startHour, fontFamily = WdxFontFamily)
                            }
                            DropdownMenu(
                                expanded = startHourDropdownExpanded,
                                onDismissRequest = { startHourDropdownExpanded = false }
                            ) {
                                horas.forEach { h ->
                                    DropdownMenuItem(
                                        text = { Text(h, fontFamily = WdxFontFamily) },
                                        onClick = {
                                            startHour = h
                                            startHourDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Text(":", fontFamily = WdxFontFamily)
                        Box {
                            Button(
                                onClick = { startMinuteDropdownExpanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(startMinute, fontFamily = WdxFontFamily)
                            }
                            DropdownMenu(
                                expanded = startMinuteDropdownExpanded,
                                onDismissRequest = { startMinuteDropdownExpanded = false }
                            ) {
                                minutosList.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m, fontFamily = WdxFontFamily) },
                                        onClick = {
                                            startMinute = m
                                            startMinuteDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    // Hora fin
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Fin:", fontFamily = WdxFontFamily)
                        Spacer(Modifier.width(8.dp))
                        Box {
                            Button(
                                onClick = { endHourDropdownExpanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(endHour, fontFamily = WdxFontFamily)
                            }
                            DropdownMenu(
                                expanded = endHourDropdownExpanded,
                                onDismissRequest = { endHourDropdownExpanded = false }
                            ) {
                                horas.forEach { h ->
                                    DropdownMenuItem(
                                        text = { Text(h, fontFamily = WdxFontFamily) },
                                        onClick = {
                                            endHour = h
                                            endHourDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Text(":", fontFamily = WdxFontFamily)
                        Box {
                            Button(
                                onClick = { endMinuteDropdownExpanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(endMinute, fontFamily = WdxFontFamily)
                            }
                            DropdownMenu(
                                expanded = endMinuteDropdownExpanded,
                                onDismissRequest = { endMinuteDropdownExpanded = false }
                            ) {
                                minutosList.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m, fontFamily = WdxFontFamily) },
                                        onClick = {
                                            endMinute = m
                                            endMinuteDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFF2F2F2),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val buttonModifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .padding(horizontal = 6.dp)

                Button(
                    onClick = onCancel,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    modifier = buttonModifier
                ) {
                    Text("Salir", fontFamily = WdxFontFamily, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                if (isEdit && onDelete != null) {
                    Button(
                        onClick = { onDelete() },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = buttonModifier
                    ) {
                        Text("Borrar", fontFamily = WdxFontFamily, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Button(
                    onClick = {
                        onSave(
                            selectedType == "timer",
                            selectedModo ?: Modo.SILENCIO,
                            if (selectedType == "timer") hours else null,
                            if (selectedType == "timer") minutes else null,
                            if (selectedType == "schedule") dayOfWeek else null,
                            if (selectedType == "schedule") "${startHour}:${startMinute}" else null,
                            if (selectedType == "schedule") "${endHour}:${endMinute}" else null
                        )
                    },
                    enabled = selectedType != null && selectedModo != null,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2FFB2)),
                    modifier = buttonModifier
                ) {
                    Text("Guardar", fontFamily = WdxFontFamily, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}