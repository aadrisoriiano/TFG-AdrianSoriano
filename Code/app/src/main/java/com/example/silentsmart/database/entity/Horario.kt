package com.example.silentsmart.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.silentsmart.Modo


@Entity(tableName = "Horario")
data class Horario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val diaSemana: String,
    val horaInicio: String,
    val horaFin: String,
    val activado: Boolean,
    val modo: Modo,

)