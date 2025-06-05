package com.example.silentsmart

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
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
        showDebugNotification(context, "AlarmReceiver", "Minuto ejecutado: ${System.currentTimeMillis()}")

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(context)
            val horarios = db.horarioDao().all.firstOrNull().orEmpty()
            val temporizadores = db.temporizadorDao().all.firstOrNull().orEmpty()

            val now = LocalTime.now()
            val today = LocalDate.now().dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
            val formatter = DateTimeFormatter.ofPattern("HH:mm")

            // Verificar horarios activos
            val horarioEnCurso = horarios.filter { it.activado }.firstOrNull { horario ->
                horario.diaSemana.equals(today, ignoreCase = true) && try {
                    val inicio = LocalTime.parse(horario.horaInicio, formatter)
                    val fin = LocalTime.parse(horario.horaFin, formatter)
                    now.isAfter(inicio) && now.isBefore(fin)
                } catch (e: Exception) {
                    false
                }
            }

            // Verificar temporizadores activos y limpiar los expirados
            val temporizadorActivo = verificarYLimpiarTemporizadores(db, temporizadores)

            Log.d("AlarmReceiver", "Temporizadores en BD: " +
                    temporizadores.joinToString { "ID=${it.id}, activo=${it.activado}, ${it.horas}h ${it.minutos}m, iniciado=${it.iniciadoEn}" })
            Log.d("AlarmReceiver", "Temporizador activo detectado: ${
                temporizadorActivo?.let { "ID=${it.id}, activo=${it.activado}, ${it.horas}h ${it.minutos}m" } ?: "Ninguno"
            }")

            val prefs = context.getSharedPreferences("audio_mode_prefs", Context.MODE_PRIVATE)

            if (horarioEnCurso != null) {
                // Si hay horario activo, guarda modo previo (solo si no estaba ya guardado)
                if (!prefs.contains("prev_ringer_mode")) {
                    AudioModeUtils.savePreviousModes(context)
                }
                AudioModeUtils.setAudioMode(context, horarioEnCurso.modo)
                showDebugNotification(context, "Cambio de modo", "Horario: ${horarioEnCurso.modo}")

            } else if (temporizadorActivo != null) {
                // Si hay temporizador activo, guarda modo previo (solo si no estaba ya guardado)
                if (!prefs.contains("prev_ringer_mode")) {
                    AudioModeUtils.savePreviousModes(context)
                }
                AudioModeUtils.setAudioMode(context, temporizadorActivo.modo)
                showDebugNotification(context, "Cambio de modo", "Temporizador: ${temporizadorActivo.modo}")

            } else {
                // No hay horario ni temporizador activo, restaurar modo anterior si lo hay
                if (prefs.contains("prev_ringer_mode")) {
                    AudioModeUtils.restorePreviousModes(context)
                    showDebugNotification(context, "Cambio de modo", "Restaurado modo anterior")
                }
            }

            // Verificar si quedan tareas activas después de la limpieza
            val horariosActivos = db.horarioDao().all.firstOrNull()?.any { it.activado } == true
            val temporizadoresActivos = db.temporizadorDao().all.firstOrNull()?.any { it.activado } == true

            // Cancela la alarma si ya no hay tareas activas
            if (!horariosActivos && !temporizadoresActivos) {
                Log.d("AlarmReceiver", "No hay más tareas activas, cancelando alarma")
                AlarmUtils.cancelAlarm(context)
            } else {
                Log.d("AlarmReceiver", "Hay tareas activas, reprogramando alarma")
                AlarmUtils.scheduleAlarm(context)
            }
        }
    }

    /**
     * Verifica qué temporizadores siguen realmente activos y desactiva los que han expirado
     * @return El primer temporizador que sigue activo, o null si no hay ninguno
     */
    private suspend fun verificarYLimpiarTemporizadores(
        db: AppDatabase,
        temporizadores: List<com.example.silentsmart.database.entity.Temporizador>
    ): com.example.silentsmart.database.entity.Temporizador? {

        val ahora = System.currentTimeMillis()
        var temporizadorValido: com.example.silentsmart.database.entity.Temporizador? = null

        temporizadores.filter { it.activado }.forEach { temporizador ->

            // Si no tiene timestamp de inicio, lo consideramos inválido y lo desactivamos
            if (temporizador.iniciadoEn == null) {
                Log.d("AlarmReceiver", "Temporizador ${temporizador.id} sin timestamp de inicio, desactivando")
                db.temporizadorDao().update(temporizador.copy(activado = false))
                return@forEach
            }

            // Calcular si el temporizador ha expirado
            val tiempoTranscurrido = ahora - temporizador.iniciadoEn
            val duracionTotal = (temporizador.horas * 3600 + temporizador.minutos * 60) * 1000L

            if (tiempoTranscurrido >= duracionTotal) {
                // El temporizador ha terminado, desactivarlo
                Log.d("AlarmReceiver", "Temporizador ${temporizador.id} ha expirado (${tiempoTranscurrido}ms >= ${duracionTotal}ms), desactivando")
                db.temporizadorDao().update(temporizador.copy(activado = false, iniciadoEn = null))
            } else {
                // El temporizador sigue activo
                val tiempoRestante = duracionTotal - tiempoTranscurrido
                Log.d("AlarmReceiver", "Temporizador ${temporizador.id} sigue activo, faltan ${tiempoRestante}ms")

                // Solo tomar el primer temporizador válido que encontremos
                if (temporizadorValido == null) {
                    temporizadorValido = temporizador
                }
            }
        }

        return temporizadorValido
    }
}

private fun showDebugNotification(context: Context, title: String, message: String) {
    Log.d("NOTI", "Intentando mostrar notificación: $title - $message")

    val channelId = "debug_channel2"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Debug",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}