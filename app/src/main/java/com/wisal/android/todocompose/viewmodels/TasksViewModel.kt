package com.wisal.android.todocompose.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.wisal.android.todocompose.data.Task
import com.wisal.android.todocompose.repositories.Repository
import com.wisal.android.todocompose.util.TasksFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    val repository: Repository
): ViewModel() {

    private val TAG = "TasksViewModel"

    private val _taskFilterType = MutableStateFlow(TasksFilterType.ALL_TASKS)
    val tasksFilterType = _taskFilterType.asStateFlow()

    val tasks: StateFlow<List<Task>> = _taskFilterType.flatMapLatest {
        repository.getAllTasks().map {
            Log.d(TAG,"Filtering called.....")
            filterTasks(it)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        Log.d(TAG,"Init")
        loadAllTasks(true)
    }

    fun loadAllTasks(update: Boolean) {
        _taskFilterType.tryEmit(tasksFilterType.value)

    }

    fun setTaskFilterType(type: TasksFilterType) {
        _taskFilterType.tryEmit(type)
        loadAllTasks(true)
    }

    private fun filterTasks(tasks: List<Task>): List<Task> {
        return when(tasksFilterType.value) {
            TasksFilterType.ALL_TASKS -> { tasks }
            TasksFilterType.ACTIVE_TASKS -> {
                tasks.filter { !it.completed }
            }
            TasksFilterType.COMPLETED_TASKS -> {
                tasks.filter { it.completed }
            }
        }
    }


    fun updateCompleted(taskId: String, completed: Boolean) = viewModelScope.launch {
        repository.updateCompleted(taskId = taskId, completed = completed)
    }

    fun clearCompleteTasks() = viewModelScope.launch {
        repository.clearCompletedTasks()
    }

}