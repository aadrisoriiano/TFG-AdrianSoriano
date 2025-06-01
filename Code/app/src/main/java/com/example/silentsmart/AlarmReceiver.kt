package com.example.silentsmart

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.silentsmart.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(context)
            val horarios = db.horarioDao().all.firstOrNull().orEmpty()
            val temporizadores = db.temporizadorDao().all.firstOrNull().orEmpty()

            // --- Lógica para horarios activos ---
            val now = LocalTime.now()
            val today = LocalDate.now().dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val horarioEnCurso = horarios.filter { it.activado }.firstOrNull { horario ->
                horario.diaSemana.equals(today, ignoreCase = true)
                        && try {
                    val inicio = LocalTime.parse(horario.horaInicio, formatter)
                    val fin = LocalTime.parse(horario.horaFin, formatter)
                    now.isAfter(inicio) && now.isBefore(fin)
                } catch (e: Exception) {
                    false
                }
            }
            if (horarioEnCurso != null) {
                // Cambia el modo de audio aquí
                // setAudioMode(context, horarioEnCurso.modo)
            }

            // --- Lógica para temporizadores activos ---
            val temporizadorActivo = temporizadores.firstOrNull { it.activado }
            if (temporizadorActivo != null) {
                // Aquí puedes comprobar si ha terminado y lanzar notificación o acción
            }

            // Si ya no hay horarios ni temporizadores activos, cancela la alarma
            if (horarios.none { it.activado } && temporizadores.none { it.activado }) {
                AlarmUtils.cancelAlarm(context)
            }
        }
    }
}