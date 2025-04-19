package com.example.silentsmart.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Horario")
data class Horario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val diaSemana: String,
    val hora: String,
    val activado: Boolean
)