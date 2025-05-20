package com.example.silentsmart.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.silentsmart.database.dao.HorarioDao
import com.example.silentsmart.database.dao.TemporizadorDao
import com.example.silentsmart.database.entity.Horario
import com.example.silentsmart.database.entity.Temporizador

@Database(entities = [Horario::class, Temporizador::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun horarioDao(): HorarioDao
    abstract fun temporizadorDao(): TemporizadorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "silentsmart_db"
                ).fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
