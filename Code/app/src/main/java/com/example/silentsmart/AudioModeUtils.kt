package com.example.silentsmart


import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.app.NotificationManager

object AudioModeUtils {
    private const val PREFS = "audio_mode_prefs"
    private const val PREV_RING = "prev_ringer_mode"
    private const val PREV_DND = "prev_dnd_mode"

    fun savePreviousModes(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(PREV_RING, audioManager.ringerMode)
            .putInt(PREV_DND, notificationManager.currentInterruptionFilter)
            .apply()
    }

    fun restorePreviousModes(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val prevRing = prefs.getInt(PREV_RING, -1)
        val prevDnd = prefs.getInt(PREV_DND, -1)
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (prevRing != -1) audioManager.ringerMode = prevRing
        if (prevDnd != -1) notificationManager.setInterruptionFilter(prevDnd)
        prefs.edit().clear().apply()
    }

    fun clearPreviousModes(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().apply()
    }

    fun setAudioMode(context: Context, modo: Modo) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            // Aquí podrías lanzar una notificación que lleve a los ajustes, pero no una Activity directamente desde el receiver
            return
        }
        when (modo) {
            Modo.SILENCIO -> audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            Modo.VIBRACION -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            Modo.SONIDO -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
    }
}
