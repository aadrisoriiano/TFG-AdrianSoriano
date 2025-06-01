package com.example.silentsmart

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.app.NotificationManager
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.silentsmart.database.AppDatabase
import com.example.silentsmart.database.entity.Horario
import com.example.silentsmart.database.entity.Temporizador
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    val horarios = db.horarioDao().all
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val temporizadores = db.temporizadorDao().all
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // --- NUEVO: Estado para temporizador activo y cuenta atrás ---
    private val _activeTimer = MutableStateFlow<Temporizador?>(null)
    val activeTimer: StateFlow<Temporizador?> = _activeTimer

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private var timerJob: Job? = null
    private var scheduleJob: Job? = null

    private var previousRingerMode: Int? = null
    private var previousDndMode: Int? = null

    init {
        //deleteAllHorarios()
        preCargarSiEsNecesario()
        startScheduleChecker()
    }

    // --- Comprobación periódica de horarios ---
    private fun startScheduleChecker() {
        scheduleJob?.cancel()
        scheduleJob = viewModelScope.launch {
            while (true) {
                checkAndApplyActiveScheduleOrTimer()
                delay(60_000) // Comprobar cada minuto
            }
        }
    }

    private fun checkAndApplyActiveScheduleOrTimer() {
        val context = getApplication<Application>().applicationContext
        val horariosActivos = horarios.value?.filterNotNull()?.filter { it.activado } ?: emptyList()
        val now = LocalTime.now()
        val today = LocalDate.now().dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val horarioEnCurso = horariosActivos?.firstOrNull { horario ->
            horario?.diaSemana.equals(today, ignoreCase = true)
                    && try {
                        val inicio = LocalTime.parse(horario?.horaInicio, formatter)
                        val fin = LocalTime.parse(horario?.horaFin, formatter)
                        now.isAfter(inicio) && now.isBefore(fin)
                    } catch (e: Exception) {
                        false
                    }
        }

        if (horarioEnCurso != null) {
            // Si hay horario activo, guarda el modo anterior solo si no hay timer ni modo guardado
            if (!_isTimerRunning.value && previousRingerMode == null && previousDndMode == null) {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                previousRingerMode = audioManager.ringerMode
                previousDndMode = notificationManager.currentInterruptionFilter
            }
            setAudioMode(context, horarioEnCurso.modo)
        } else {
            // Si NO hay horario activo, pero hay temporizador activo, manda el temporizador
            val timer = _activeTimer.value
            val running = _isTimerRunning.value
            if (timer != null && running && _remainingSeconds.value > 0) {
                setAudioMode(context, timer.modo)
            } else {
                // Si no hay ni horario ni temporizador, restaura el modo anterior
                restorePreviousAudioMode(context)
            }
        }
    }

    // --- Cambia el estado del horario ---
    fun setHorarioActivado(horario: Horario, activado: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            db.horarioDao().update(horario.copy(activado = activado))
            actualizarAlarmManager()
        }
    }

    fun startTimer(temporizador: Temporizador, context: Context, reset: Boolean = true) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (reset && _activeTimer.value == null && previousRingerMode == null && previousDndMode == null) {
            previousRingerMode = audioManager.ringerMode
            previousDndMode = notificationManager.currentInterruptionFilter
        }

        _activeTimer.value = temporizador

        if (reset) {
            _remainingSeconds.value = (temporizador.horas * 3600) + (temporizador.minutos * 60)
        }
        _isTimerRunning.value = true

        setAudioMode(context, temporizador.modo)

        // --- PROGRAMA EL ALARM MANAGER SOLO SI EL TEMPORIZADOR ESTÁ CORRIENDO ---
        actualizarAlarmManager()

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _remainingSeconds.value -= 1
            }
            _isTimerRunning.value = false
            _activeTimer.value = null

            // Restaurar solo si había modo previo guardado
            val ctx = getApplication<Application>().applicationContext
            if (previousRingerMode != null || previousDndMode != null) {
                restorePreviousAudioMode(ctx)
            }
            checkAndApplyActiveScheduleOrTimer()
            // --- CANCELA EL ALARM MANAGER AL TERMINAR ---
            actualizarAlarmManager()
        }
    }


    fun pauseOrResumeTimer(context: Context) {
        _isTimerRunning.value = !_isTimerRunning.value
        if (_isTimerRunning.value) {
            // Al reanudar, NUNCA resetear ni guardar modo previo
            startTimer(_activeTimer.value!!, context, reset = false)
        } else {
            timerJob?.cancel()
            // --- CANCELA EL ALARM MANAGER AL PAUSAR ---
            actualizarAlarmManager()
        }
    }


    fun stopTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
        _activeTimer.value = null
        _remainingSeconds.value = 0
        checkAndApplyActiveScheduleOrTimer()
        // --- CANCELA EL ALARM MANAGER AL PARAR ---
        actualizarAlarmManager()
    }

    fun requestDoNotDisturbPermission(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun setAudioMode(context: Context, modo: Modo) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            requestDoNotDisturbPermission(context)
            return
        }
        when (modo) {
            Modo.SILENCIO -> audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            Modo.VIBRACION -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            Modo.SONIDO -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
    }

    fun restorePreviousAudioMode(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        previousRingerMode?.let { audioManager.ringerMode = it }
        previousDndMode?.let { notificationManager.setInterruptionFilter(it) }
        previousRingerMode = null
        previousDndMode = null
    }

    private fun preCargarSiEsNecesario() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val horarios = db.horarioDao().all.firstOrNull().orEmpty()
                val temporizadores = db.temporizadorDao().all.firstOrNull().orEmpty()

                if (horarios.isEmpty()) {
                    db.horarioDao().insert(Horario(diaSemana = "Lunes", horaInicio = "08:00", horaFin = "09:00", activado = false, modo = Modo.SILENCIO))
                    db.horarioDao().insert(Horario(diaSemana = "Martes", horaInicio = "19:05", horaFin = "19:07", activado = false, modo = Modo.VIBRACION))
                    db.horarioDao().insert(Horario(diaSemana = "Miércoles", horaInicio = "14:00", horaFin = "15:00", activado = false, modo = Modo.SONIDO))
                    db.horarioDao().insert(Horario(diaSemana = "Sábado", horaInicio = "14:10", horaFin = "14:15", activado = false, modo = Modo.SILENCIO))
                    db.horarioDao().insert(Horario(diaSemana = "Sábado", horaInicio = "14:00", horaFin = "15:00", activado = false, modo = Modo.SILENCIO))
                    db.horarioDao().insert(Horario(diaSemana = "Sábado", horaInicio = "14:00", horaFin = "15:00", activado = false, modo = Modo.SILENCIO))
                }

                if (temporizadores.isEmpty()) {
                    db.temporizadorDao().insert(Temporizador(horas = 1, minutos = 0, activado = false, modo = Modo.SILENCIO))
                    db.temporizadorDao().insert(Temporizador(horas = 0, minutos = 45, activado = false, modo = Modo.SILENCIO))
                    db.temporizadorDao().insert(Temporizador(horas = 2, minutos = 15, activado = false, modo = Modo.SONIDO))
                    db.temporizadorDao().insert(Temporizador(horas = 0, minutos = 1, activado = false, modo = Modo.VIBRACION))
                }
            }
        }
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

    fun addRegistro(
        isTimer: Boolean,
        modo: Modo,
        hours: Int?,
        minutes: Int?,
        day: String?,
        startHour: String?,
        endHour: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isTimer) {
                db.temporizadorDao().insert(
                    Temporizador(
                        horas = hours ?: 0,
                        minutos = minutes ?: 0,
                        activado = false,
                        modo = modo
                    )
                )
            } else {
                db.horarioDao().insert(
                    Horario(
                        diaSemana = day ?: "Monday",
                        horaInicio = startHour ?: "08:00",
                        horaFin = endHour ?: "09:00",
                        activado = false,
                        modo = modo
                    )
                )
            }
        }
    }

    fun updateTemporizador(temporizador: Temporizador) {
        viewModelScope.launch(Dispatchers.IO) {
            db.temporizadorDao().update(temporizador)
            actualizarAlarmManager()
        }
    }

    fun updateHorario(horario: Horario) {
        viewModelScope.launch(Dispatchers.IO) {
            db.horarioDao().update(horario)
        }
    }

    fun deleteTemporizador(temporizador: Temporizador) {
        viewModelScope.launch(Dispatchers.IO) {
            db.temporizadorDao().delete(temporizador)
            actualizarAlarmManager()
        }
    }

    fun deleteHorario(horario: Horario) {
        viewModelScope.launch(Dispatchers.IO) {
            db.horarioDao().delete(horario)
            actualizarAlarmManager()
        }
    }

    fun toggleHorarioFavorito(horario: Horario) {
        viewModelScope.launch(Dispatchers.IO) {
            db.horarioDao().update(horario.copy(favorito = !horario.favorito))
        }
    }

    fun toggleTemporizadorFavorito(temporizador: Temporizador) {
        viewModelScope.launch(Dispatchers.IO) {
            db.temporizadorDao().update(temporizador.copy(favorito = !temporizador.favorito))
        }
    }

    private suspend fun hayAlgunoActivo(): Boolean {
        val horariosActivos = db.horarioDao().all.firstOrNull()?.any { it.activado } == true
        val temporizadoresActivos = db.temporizadorDao().all.firstOrNull()?.any { it.activado } == true
        return horariosActivos || temporizadoresActivos
    }

    private fun actualizarAlarmManager() {
        val context = getApplication<Application>().applicationContext
        viewModelScope.launch(Dispatchers.IO) {
            if (hayAlgunoActivo()) {
                AlarmUtils.scheduleAlarm(context)
            } else {
                AlarmUtils.cancelAlarm(context)
            }
        }
    }
}
