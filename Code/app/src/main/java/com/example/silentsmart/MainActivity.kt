package com.example.silentsmart


import BottomNavigationBarWithDivider
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.silentsmart.database.entity.Horario
import com.example.silentsmart.database.entity.Temporizador

import com.example.silentsmart.ui.theme.SilentSmartTheme
import com.example.silentsmart.Header

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SilentSmartTheme {
                val mainViewModel = remember { MainViewModel(application) }
                var selectedTab by remember { mutableStateOf("Temporizador") }
                var showAddEdit by remember { mutableStateOf(false) }
                var editTimer: Temporizador? by remember { mutableStateOf(null) }
                var editHorario: Horario? by remember { mutableStateOf(null) }
                var editMode by remember { mutableStateOf(false) }

                Scaffold(
                    bottomBar = {
                        if (!showAddEdit) {
                            BottomNavigationBar(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it }
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color.White)
                            .navigationBarsPadding()
                    ) {
                        if (showAddEdit) {
                            AddEditScreen(
                                isEdit = editTimer != null || editHorario != null,
                                initialType = when {
                                    editTimer != null -> "timer"
                                    editHorario != null -> "schedule"
                                    else -> null
                                },
                                initialModo = editTimer?.modo ?: editHorario?.modo,
                                initialHours = editTimer?.horas,
                                initialMinutes = editTimer?.minutos,
                                initialDay = editHorario?.diaSemana,
                                initialStartHour = editHorario?.horaInicio,
                                initialEndHour = editHorario?.horaFin,
                                onSave = { isTimer, modo, hours, minutes, day, startHour, endHour ->
                                    if (isTimer && editTimer != null) {
                                        mainViewModel.updateTemporizador(
                                            editTimer!!.copy(
                                                horas = hours ?: 0,
                                                minutos = minutes ?: 0,
                                                modo = modo
                                            )
                                        )
                                    } else if (!isTimer && editHorario != null) {
                                        mainViewModel.updateHorario(
                                            editHorario!!.copy(
                                                diaSemana = day ?: "Lunes",
                                                horaInicio = startHour ?: "08:00",
                                                horaFin = endHour ?: "09:00",
                                                modo = modo
                                            )
                                        )
                                    } else {
                                        mainViewModel.addRegistro(isTimer, modo, hours, minutes, day, startHour, endHour)
                                    }
                                    showAddEdit = false
                                    editTimer = null
                                    editHorario = null
                                    editMode = false
                                },
                                onCancel = {
                                    showAddEdit = false
                                    editTimer = null
                                    editHorario = null
                                    editMode = false
                                },
                                onDelete = if (editTimer != null) {
                                    {
                                        mainViewModel.deleteTemporizador(editTimer!!)
                                        showAddEdit = false
                                        editTimer = null
                                        editMode = false
                                    }
                                } else if (editHorario != null) {
                                    {
                                        mainViewModel.deleteHorario(editHorario!!)
                                        showAddEdit = false
                                        editHorario = null
                                        editMode = false
                                    }
                                } else null
                            )
                        } else {
                            Header(
                                title = selectedTab,
                                viewModel = mainViewModel,
                                onAddClick = { 
                                    editTimer = null
                                    editHorario = null
                                    showAddEdit = true 
                                },
                                onEditClick = { editMode = !editMode },
                                isEditMode = editMode // <--- Añade esto
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            when (selectedTab) {
                                "Temporizador" -> TimerContent(
                                    viewModel = mainViewModel,
                                    editMode = editMode,
                                    onTimerSelected = { temporizador ->
                                        editTimer = temporizador
                                        showAddEdit = true
                                        editMode = false
                                    }
                                )
                                "Horario" -> ScheduleContent(
                                    viewModel = mainViewModel,
                                    editMode = editMode,
                                    onHorarioSelected = { horario ->
                                        editHorario = horario
                                        showAddEdit = true
                                        editMode = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
