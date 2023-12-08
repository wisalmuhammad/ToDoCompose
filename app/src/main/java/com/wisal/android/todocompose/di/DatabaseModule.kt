package com.wisal.android.todocompose.di

import android.content.Context
import androidx.compose.runtime.remember
import androidx.room.Room
import com.wisal.android.todocompose.data.source.TasksDao
import com.wisal.android.todocompose.data.source.ToDoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideTasksDatabase(@ApplicationContext context: Context): ToDoDatabase {
        return Room
            .databaseBuilder(
                context.applicationContext,
                ToDoDatabase::class.java,
                "ToDoTasks.db"
            ).build()
    }

    @Provides
    @Singleton
    fun provideTasksDao(database: ToDoDatabase): TasksDao {
        return database.tasksDao()
    }

}