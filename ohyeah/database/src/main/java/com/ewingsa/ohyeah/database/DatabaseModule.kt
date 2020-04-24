package com.ewingsa.ohyeah.database

import android.app.Application
import androidx.room.Room
import com.ewingsa.ohyeah.appinjection.scopes.PerApplication
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @PerApplication
    @Provides
    fun provideMessageDao(mainApplication: Application): MessageDao {
        return Room.databaseBuilder(mainApplication, AppDatabase::class.java, DATABASE_NAME).build().messageDao()
    }

    private companion object {
        const val DATABASE_NAME = "app-database"
    }
}
