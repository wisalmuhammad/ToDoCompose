package com.wisal.android.todocompose.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wisal.android.todocompose.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeleteTaskUiState(
    val isTaskDeleted: Boolean = false
)
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    val repository: Repository
): ViewModel() {

    private val _taskDeleteUpdate = MutableStateFlow(DeleteTaskUiState())
    val deleteTaskUpdate = _taskDeleteUpdate.asStateFlow()

    private val _taskId = MutableStateFlow<String?>(null)
    val task = _taskId.filterNotNull().flatMapLatest {
        repository.observeTaskById(it).map {
            println("Task loaded: $it")
            it
        }
    }

    fun loadTaskById(taskId: String) {
        _taskId.value = taskId
    }

    fun deleteTask(taskId: String) = viewModelScope.launch {
        repository.deleteTaskById(taskId)
        _taskDeleteUpdate.update { it.copy(isTaskDeleted = true) }
    }

    fun updateCompleted(taskId: String, completed: Boolean) = viewModelScope.launch {
        repository.updateCompleted(taskId = taskId, completed = completed)
    }
}