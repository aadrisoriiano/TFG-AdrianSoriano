package com.example.silentsmart.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Temporizador")
data class Temporizador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val horas: Int,
    val minutos: Int,
    val activado: Boolean
)