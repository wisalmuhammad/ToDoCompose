package com.wisal.android.todocompose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wisal.android.todocompose.Screen
import com.wisal.android.todocompose.data.Task
import com.wisal.android.todocompose.viewmodels.TaskDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    taskId: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Task Details") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = { viewModel.deleteTask(taskId) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "TODO")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "TODO")
                    }
                }
            )
        }
    ) { paddingValues ->
        val task = viewModel.task.collectAsStateWithLifecycle(initialValue = Task())
        TaskDetailScreenContent(
            task = task.value,
            modifier = modifier.padding(paddingValues = paddingValues),
            onTaskClick = {
                navController.navigate("${Screen.AddEditScreen.route}?taskId=$it")
            },
            onCheckChanged = {
                viewModel.updateCompleted(taskId,it)
            }
        )
        LaunchedEffect(key1 = Unit, block = {
            viewModel.loadTaskById(taskId)
        })
        LaunchedEffect(key1 = Unit, block = {
            viewModel.deleteTaskUpdate.collect {
                if(it.isTaskDeleted) {
                    navController.navigateUp()
                }
            }
        })
    }
}


@Composable
fun TaskDetailScreenContent(
    task: Task,
    modifier: Modifier = Modifier,
    onCheckChanged: (Boolean) -> Unit,
    onTaskClick: (String) -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(start = 8.dp, top = 8.dp)
        .clickable {
            onTaskClick.invoke(task.id)
        }
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = { onCheckChanged.invoke(it) }
        )
        Column {
         Text(
             text = task.title,
             fontSize = 19.sp,
             fontWeight = FontWeight.Bold
         )
         Text(text = task.description)
        }
    }
}