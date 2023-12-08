package com.wisal.android.todocompose.data.source

import androidx.lifecycle.LiveData
import com.wisal.android.todocompose.data.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface LocalDataSource {
    fun getAllTasks(): Flow<List<Task>>

    fun observeTaskById(taskId: String): Flow<Task>

    suspend fun updateCompleted(taskId: String, completed: Boolean)

    suspend fun saveTask(task: Task)

    suspend fun getTaskById(taskId: String): Task?

    suspend fun updateTask(task: Task)

    suspend fun deleteTaskById(taskId: String): Int
    suspend fun clearCompletedTasks(): Int

}



class LocalDataSourceImp @Inject constructor(
    val dao: TasksDao,
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LocalDataSource {

    override fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()
    override fun observeTaskById(taskId: String): Flow<Task> {
        return dao.observeTaskById(taskId)
    }

    override suspend fun updateCompleted(taskId: String, completed: Boolean) = withContext(ioDispatcher){
        dao.updateCompleted(taskId = taskId, completed = completed)
    }

    override suspend fun saveTask(task: Task) = withContext(ioDispatcher){
        dao.insertTask(task)
    }

    override suspend fun getTaskById(taskId: String): Task? = withContext(ioDispatcher){
        return@withContext dao.getTaskById(taskId)
    }

    override suspend fun updateTask(task: Task) = withContext(ioDispatcher){
        dao.updateTask(task)
    }

    override suspend fun deleteTaskById(taskId: String): Int = withContext(ioDispatcher){
        dao.deleteTaskById(taskId)
    }

    override suspend fun clearCompletedTasks(): Int = withContext(ioDispatcher){
        dao.clearCompletedTasks()
    }
}