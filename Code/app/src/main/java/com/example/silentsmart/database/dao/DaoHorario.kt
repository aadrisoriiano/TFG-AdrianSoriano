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
    @Query("SELECT * FROM Horario")
    fun getAll(): Flow<List<Horario>>

    @Update
    suspend fun update(horario: Horario)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg horarios: Horario)
}

