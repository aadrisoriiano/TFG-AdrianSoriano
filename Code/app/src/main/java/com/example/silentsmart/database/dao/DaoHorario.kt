package com.example.silentsmart.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.silentsmart.database.entity.Horario
import kotlinx.coroutines.flow.Flow

@Dao
interface HorarioDao {
    @get:Query("SELECT * FROM Horario ORDER BY favorito DESC, id ASC")
    val all: Flow<List<Horario>>

    //@Query("SELECT * FROM Horario WHERE id = :id")
   // fun get(id: Int): Horario

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insert(horario: Horario?)

    @Update
    fun update(horario: Horario?)

    @Delete
    fun delete(horario: Horario?)
    @Query("DELETE FROM horario")
     fun deleteAll()
}