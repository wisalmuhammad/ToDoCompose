package com.wisal.android.todocompose.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wisal.android.todocompose.data.Task
import com.wisal.android.todocompose.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditUiState(
    val isTaskAdded : Boolean = false
)
@HiltViewModel
class AddEditViewModel @Inject constructor(
    val repository: Repository
): ViewModel() {

    private val TAG = "AddEditViewModel"

    private val _taskTitle = MutableStateFlow("")
    val taskTitle = _taskTitle.asStateFlow()

    private val _taskDetail = MutableStateFlow("")
    val taskDetail = _taskDetail.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    private val _updateTaskState = MutableStateFlow(AddEditUiState())
    val updateTaskState = _updateTaskState.asStateFlow()

    private var task: Task? = null
    private var isNewTask = false
    private var taskId: String? = null

//    fun showSnackbar(message: String) {
//        _snackbarMessage.value = message
//    }

    fun fetchTaskById(taskId: String?) {
        this.taskId = taskId
        if(taskId == null) {
            isNewTask = true
            return
        }
        isNewTask = false
        viewModelScope.launch {
            task = repository.getTaskById(taskId)
            if(task != null) {
                _taskTitle.value = task!!.title
                _taskDetail.value = task!!.description
            }
        }
    }
    fun updateTaskTitle(newText: String) {
        _taskTitle.value = newText
    }

    fun updateTaskDetail(newText: String) {
        _taskDetail.value = newText
    }

    fun saveTask() = viewModelScope.launch {
        if (_taskTitle.value.isEmpty()) {
            _snackbarMessage.value = "Please fill all task details"
            return@launch
        }
        if(isNewTask || taskId == null){
            val task = Task(title = _taskTitle.value, description = _taskDetail.value)
            createNewTask(task = task)
        } else {
            val task = Task(id = taskId!!, title = _taskTitle.value, description = _taskDetail.value, completed = task?.completed ?: false )
            updateTask(task = task)
        }
    }

    private suspend fun createNewTask(task: Task) {
        repository.saveTask(task = task)
        _updateTaskState.update { it.copy(isTaskAdded = true) }
    }

    private suspend fun updateTask(task: Task) {
        repository.updateTask(task)
        _updateTaskState.update { it.copy(isTaskAdded = true) }
    }

}