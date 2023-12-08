package com.wisal.android.todocompose.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wisal.android.todocompose.data.Task


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun tasksDao(): TasksDao

}