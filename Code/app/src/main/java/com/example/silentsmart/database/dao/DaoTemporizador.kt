package com.example.silentsmart.database.dao

import androidx.room.*
import com.example.silentsmart.database.entity.Temporizador
import kotlinx.coroutines.flow.Flow

@Dao
interface TemporizadorDao {
    @Query("SELECT * FROM Temporizador")
    fun getAll(): Flow<List<Temporizador>>

    @Update
    suspend fun update(temporizador: Temporizador)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg temporizadores: Temporizador)
}