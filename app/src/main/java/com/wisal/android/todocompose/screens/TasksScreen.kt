package com.wisal.android.todocompose.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wisal.android.todocompose.R
import com.wisal.android.todocompose.Screen
import com.wisal.android.todocompose.data.Task
import com.wisal.android.todocompose.util.TasksFilterType
import com.wisal.android.todocompose.viewmodels.TasksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavHostController,
    viewModel: TasksViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = stringResource(id = R.string.todo)) },
                navigationIcon = {
                    IconButton(
                        onClick = {  }
                    ){
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "TODO"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "TODO"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        // 6
                        DropdownMenuItem(
                            text = {
                                Text("All")
                            },
                            onClick = {
                                viewModel.setTaskFilterType(TasksFilterType.ALL_TASKS)
                                expanded = false
                                      },
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Active")
                            },
                            onClick = {
                                viewModel.setTaskFilterType(TasksFilterType.ACTIVE_TASKS)
                                expanded = false
                                      },
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Completed")
                            },
                            onClick = {
                                viewModel.setTaskFilterType(TasksFilterType.COMPLETED_TASKS)
                                expanded = false
                                      },
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Clear completed")
                            },
                            onClick = {
                                viewModel.clearCompleteTasks()
                                expanded = false
                                      },
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditScreen.route)
                },
                shape = CircleShape) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "TODO"
                )
            }
        }
    ) { paddingValues ->
        TaskScreenContent(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            viewModel = viewModel
        )
    }
    LaunchedEffect(viewModel.tasks) {
        Log.d("TasksScreen", "Recomposed")
    }
}

@Composable
fun TaskScreenContent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel
) {
    println("TaskScreen content called......")
    val tasks = viewModel.tasks.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
    ) {
        Text(
            text = "All Tasks",
            fontSize = 18.sp,
            fontFamily = FontFamily.Default,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth()
        )
        LazyColumn {
            items(tasks.value,key = { task ->
                task.id
            }) {
                TaskRow(task = it,
                    onTaskClick = { taskId ->
                        navController.navigate("${Screen.TaskDetail.route}?taskId=$taskId")
                    },
                onCheckClick = { taskId,isChecked ->
                    viewModel.updateCompleted(taskId = taskId,isChecked)
                })
            }
        }

    }
}

@Composable
fun TaskRow(task: Task,modifier: Modifier = Modifier,onTaskClick: (String) -> Unit,onCheckClick: (String,Boolean) -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onTaskClick.invoke(task.id)
            },
        verticalAlignment = CenterVertically
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = { onCheckClick.invoke(task.id,it) },
            modifier = modifier
                .align(CenterVertically)
                .padding(top = 8.dp, bottom = 8.dp)
        )
        Text(
            text = task.title,
            modifier = Modifier
                .align(CenterVertically)
                .padding(start = 8.dp), // Add padding to create space between Checkbox and Text
            fontSize = 16.sp
        )
    }
}