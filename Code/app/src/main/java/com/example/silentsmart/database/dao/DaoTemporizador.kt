package com.example.silentsmart.database.dao

import androidx.room.*
import com.example.silentsmart.database.entity.Temporizador
import kotlinx.coroutines.flow.Flow

@Dao
interface TemporizadorDao {
    @get:Query("SELECT * FROM Temporizador ORDER BY favorito DESC, id ASC")
    val all: Flow<List<Temporizador>>

   // @Query("SELECT * FROM Temporizador WHERE id = :id")
    //fun get(id: Int): Temporizador?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insert(temporizador: Temporizador?)

    @Update
    fun update(temporizador: Temporizador?)


    @Query("UPDATE Temporizador SET activado = 0")
    fun desactivarTodos()


    @Delete
    fun delete(temporizador: Temporizador?)

    @Query("DELETE FROM temporizador")
     fun deleteAll()
}