package com.wisal.android.todocompose.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wisal.android.todocompose.repositories.Repository
import com.wisal.android.todocompose.util.StatsResult
import com.wisal.android.todocompose.util.getActiveAndCompletedTasksStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "StatisticsViewModel"

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val repository: Repository
): ViewModel() {

    val stats: StateFlow<StatsResult> = repository.getAllTasks()
        .map {
            getActiveAndCompletedTasksStats(
                it
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StatsResult(activeTasksPercent = 0f, completedTasksPercent = 0f)
        )

}