package com.example.silentsmart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.silentsmart.database.AppDatabase
import com.example.silentsmart.database.entity.Horario
import com.example.silentsmart.database.entity.Temporizador
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.apply
import kotlin.text.insert


@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    val horarios = db.horarioDao().all
        .flowOn(Dispatchers.IO) // Ejecutar en un hilo de fondo
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val temporizadores = db.temporizadorDao().all
        .flowOn(Dispatchers.IO) // Ejecutar en un hilo de fondo
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    init {
        //deleteAllHorarios()
        preCargarSiEsNecesario()

    }
    private fun deleteAllHorarios() {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("app_prefs", Application.MODE_PRIVATE)
        val datosPrecargados = sharedPreferences.getBoolean("datos_precargados", false)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.temporizadorDao().deleteAll()
                db.horarioDao().deleteAll()
                sharedPreferences.edit().putBoolean("datos_precargados", false).apply()

            }
        }
    }
    private fun preCargarSiEsNecesario() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Obtener todos los horarios y temporizadores de la base de datos
                val horarios = db.horarioDao().all.firstOrNull().orEmpty()
                val temporizadores = db.temporizadorDao().all.firstOrNull().orEmpty()

                // Si la tabla de horarios está vacía, insertar ejemplos
                if (horarios.isEmpty()) {
                    db.horarioDao().insert(Horario(diaSemana = "Lunes", horaInicio = "08:00", horaFin = "09:00", activado = false, modo = Modo.SILENCIO))
                    db.horarioDao().insert(Horario(diaSemana = "Martes", horaInicio = "10:00", horaFin = "11:00", activado = false, modo = Modo.VIBRACION))
                    db.horarioDao().insert(Horario(diaSemana = "Miércoles", horaInicio = "14:00", horaFin = "15:00", activado = false, modo = Modo.SONIDO))
                }

                // Si la tabla de temporizadores está vacía, insertar ejemplos
                if (temporizadores.isEmpty()) {
                    db.temporizadorDao().insert(Temporizador(horas = 1, minutos = 0, activado = false, modo = Modo.SILENCIO))
                    db.temporizadorDao().insert(Temporizador(horas = 0, minutos = 45, activado = false, modo = Modo.SILENCIO))
                    db.temporizadorDao().insert(Temporizador(horas = 2, minutos = 15, activado = false, modo = Modo.SONIDO))
                    db.temporizadorDao().insert(Temporizador(horas = 5, minutos = 25, activado = false, modo = Modo.VIBRACION))
                }
            }
        }
    }


    fun startTemporizador(temporizador: Temporizador) {
        viewModelScope.launch {
            // Por ahora solo ejemplo, luego podrías lanzar una cuenta regresiva o alarma
            db.temporizadorDao().update(temporizador.copy(activado = true))
        }
    }
}
