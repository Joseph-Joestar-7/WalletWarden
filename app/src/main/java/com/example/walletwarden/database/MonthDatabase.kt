package com.example.walletwarden.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MonthEntity::class], version = 1, exportSchema = false)
abstract class MonthDatabase : RoomDatabase() {
    abstract fun monthDao(): MonthDao

    companion object {
        @Volatile
        private var Instance: MonthDatabase? = null

        fun getDatabase(context: Context): MonthDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MonthDatabase::class.java, "month_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}