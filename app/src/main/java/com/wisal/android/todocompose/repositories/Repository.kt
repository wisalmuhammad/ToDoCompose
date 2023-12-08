package com.wisal.android.todocompose.repositories

import com.wisal.android.todocompose.data.Task
import com.wisal.android.todocompose.data.source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface Repository {
    fun getAllTasks(): Flow<List<Task>>
    fun observeTaskById(taskId: String): Flow<Task>
    suspend fun updateCompleted(taskId: String, completed: Boolean)
    suspend fun saveTask(task: Task)
    suspend fun getTaskById(taskId: String): Task?
    suspend fun updateTask(task: Task)
    suspend fun deleteTaskById(taskId: String): Int
    suspend fun clearCompletedTasks(): Int

}



class RepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource
): Repository {
    override fun getAllTasks(): Flow<List<Task>> {
        return localDataSource.getAllTasks()
    }

    override fun observeTaskById(taskId: String): Flow<Task> {
        return localDataSource.observeTaskById(taskId)
    }


    override suspend fun updateCompleted(taskId: String, completed: Boolean) {
        localDataSource.updateCompleted(taskId = taskId, completed = completed)
    }

    override suspend fun saveTask(task: Task) {
        localDataSource.saveTask(task = task)
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return localDataSource.getTaskById(taskId)
    }

    override suspend fun updateTask(task: Task) {
        localDataSource.updateTask(task)
    }

    override suspend fun deleteTaskById(taskId: String): Int {
        return localDataSource.deleteTaskById(taskId = taskId)
    }

    override suspend fun clearCompletedTasks(): Int {
        return localDataSource.clearCompletedTasks()
    }

}