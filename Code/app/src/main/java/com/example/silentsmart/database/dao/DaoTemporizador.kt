package com.example.silentsmart.database.dao

import androidx.room.*
import com.example.silentsmart.database.entity.Temporizador

@Dao
interface TemporizadorDao {

    @Query("SELECT * FROM Temporizador")
    suspend fun getAll(): List<Temporizador>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(temporizador: Temporizador): Long

    @Update
    suspend fun update(temporizador: Temporizador)

    @Delete
    suspend fun delete(temporizador: Temporizador)

    @Query("SELECT * FROM Temporizador WHERE activado = 1 LIMIT 1")
    suspend fun getTemporizadorActivo(): Temporizador?
}
