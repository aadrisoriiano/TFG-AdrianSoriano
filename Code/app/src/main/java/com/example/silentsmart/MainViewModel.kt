package com.example.silentsmart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.silentsmart.database.AppDatabase
import com.example.silentsmart.database.entity.Horario
import com.example.silentsmart.database.entity.Temporizador
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    val horarios = db.horarioDao().getAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val temporizadores = db.temporizadorDao().getAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        preCargarSiEsNecesario()
    }

    private fun preCargarSiEsNecesario() {
        viewModelScope.launch {
            if (db.horarioDao().getAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList()).value.isEmpty()) {
                db.horarioDao().insertAll(
                    Horario(diaSemana = "Lunes", horaInicio = "08:00", horaFin = "09:00", activado = false),
                    Horario(diaSemana = "Martes", horaInicio = "10:00", horaFin = "11:00", activado = false),
                    Horario(diaSemana = "Miércoles", horaInicio = "14:00", horaFin = "15:00", activado = false)
                )
            }

            if (db.temporizadorDao().getAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList()).value.isEmpty()) {
                db.temporizadorDao().insertAll(
                    Temporizador(horas = 1, minutos = 0, activado = false),
                    Temporizador(horas = 0, minutos = 45, activado = false),
                    Temporizador(horas = 2, minutos = 15, activado = false)
                )
            }
        }
    }

    fun toggleHorario(horario: Horario) {
        viewModelScope.launch {
            db.horarioDao().update(horario.copy(activado = !horario.activado))
        }
    }

    fun startTemporizador(temporizador: Temporizador) {
        viewModelScope.launch {
            // Por ahora solo ejemplo, luego podrías lanzar una cuenta regresiva o alarma
            db.temporizadorDao().update(temporizador.copy(activado = true))
        }
    }
}
