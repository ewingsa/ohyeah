package com.ewingsa.ohyeah.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Message::class, Sender::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
