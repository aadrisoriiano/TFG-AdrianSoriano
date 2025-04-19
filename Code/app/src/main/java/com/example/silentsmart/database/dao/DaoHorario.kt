package com.example.silentsmart.database.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.silentsmart.database.entity.Horario
@Dao
interface HorarioDao {

    @Query("SELECT * FROM Horario")
    suspend fun getAll(): List<Horario>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(horario: Horario): Long

    @Update
    suspend fun update(horario: Horario)

    @Delete
    suspend fun delete(horario: Horario)

    @Query("SELECT * FROM Horario WHERE activado = 1")
    suspend fun getHorariosActivos(): List<Horario>
}
