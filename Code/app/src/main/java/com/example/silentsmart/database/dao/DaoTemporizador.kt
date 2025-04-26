package com.example.silentsmart.database.dao

import androidx.room.*
import com.example.silentsmart.database.entity.Temporizador
import kotlinx.coroutines.flow.Flow

@Dao
interface TemporizadorDao {
    @get:Query("SELECT * FROM Temporizador")
    val all: Flow<MutableList<Temporizador?>?>

    @Query("SELECT * FROM Temporizador WHERE id = :id")
    fun get(id: Int): Temporizador?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insert(temporizador: Temporizador?)

    @Update
    fun update(temporizador: Temporizador?)

    @Delete
    fun delete(temporizador: Temporizador?)

    @Query("DELETE FROM temporizador")
     fun deleteAll()
}