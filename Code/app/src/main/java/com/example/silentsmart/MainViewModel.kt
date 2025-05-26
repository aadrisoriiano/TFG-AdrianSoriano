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

    private var previousRingerMode: Int? = null
    private var previousDndMode: Int? = null

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
                val horarios = db.horarioDao().all.firstOrNull().orEmpty()
                val temporizadores = db.temporizadorDao().all.firstOrNull().orEmpty()

                if (horarios.isEmpty()) {
                    db.horarioDao().insert(Horario(diaSemana = "Lunes", horaInicio = "08:00", horaFin = "09:00", activado = false, modo = Modo.SILENCIO))
                    db.horarioDao().insert(Horario(diaSemana = "Martes", horaInicio = "10:00", horaFin = "11:00", activado = false, modo = Modo.VIBRACION))
                    db.horarioDao().insert(Horario(diaSemana = "Miércoles", horaInicio = "14:00", horaFin = "15:00", activado = false, modo = Modo.SONIDO))
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

    // --- NUEVO: Lógica de temporizador activo y control ---
    fun startTimer(temporizador: Temporizador, context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Solo guarda el modo anterior si NO hay temporizador corriendo ni modo guardado
        if (!_isTimerRunning.value && previousRingerMode == null && previousDndMode == null) {
            previousRingerMode = audioManager.ringerMode
            previousDndMode = notificationManager.currentInterruptionFilter
        }

        _activeTimer.value = temporizador
        _remainingSeconds.value = (temporizador.horas * 3600) + (temporizador.minutos * 60)
        _isTimerRunning.value = true

        setAudioMode(context, temporizador.modo)

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _remainingSeconds.value -= 1
            }
            _isTimerRunning.value = false

            // Restaura el modo anterior al terminar el timer
            restorePreviousAudioMode(context)
        }
    }

    fun pauseOrResumeTimer() {
        _isTimerRunning.value = !_isTimerRunning.value
        if (_isTimerRunning.value) {
            timerJob?.cancel()
            timerJob = viewModelScope.launch {
                while (_remainingSeconds.value > 0 && _isTimerRunning.value) {
                    delay(1000)
                    _remainingSeconds.value -= 1
                }
                _isTimerRunning.value = false
            }
        } else {
            timerJob?.cancel()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
        _activeTimer.value = null
        _remainingSeconds.value = 0
    }

    // --- (Opcional) Mantén tu función original para compatibilidad ---
    fun startTemporizador(temporizador: Temporizador) {
        viewModelScope.launch {
            db.temporizadorDao().update(temporizador.copy(activado = true))
        }
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
}
