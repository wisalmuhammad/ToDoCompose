package com.wisal.android.todocompose.data.source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wisal.android.todocompose.data.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TasksDao {

    @Query("SELECT * FROM Tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    fun getTaskById(taskId: String): Task?

    @Query("SELECT * FROM Tasks")
    fun getTasks(): List<Task>

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    fun observeTaskById(taskId: String): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(task: Task)

    @Query("UPDATE Tasks SET completed = :completed WHERE id = :taskId")
    fun updateCompleted(taskId: String, completed: Boolean)

    @Update
    suspend fun updateTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTasks(vararg task: Task)

    @Query("DELETE FROM Tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM Tasks")
    fun deleteAllTasks()

    @Query("DELETE FROM Tasks where completed = 1")
    suspend fun clearCompletedTasks(): Int

}