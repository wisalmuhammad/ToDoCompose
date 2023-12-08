package com.wisal.android.todocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wisal.android.todocompose.screens.TaskDetailScreen
import com.wisal.android.todocompose.screens.TasksScreen
import com.wisal.android.todocompose.ui.theme.ToDoComposeTheme
import dagger.hilt.android.AndroidEntryPoint


sealed class Screen(val title: String,val route: String) {
    object TasksScreen: Screen("Todo","tasks")
    object AddEditScreen: Screen("New Task","add_edit_task")
    object TaskDetail: Screen("Task Detail","task_detail")
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoComposeTheme {
                ToDoApp()
            }
        }
    }
}

@Composable
fun ToDoApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.TasksScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        builder = {
            composable(Screen.TasksScreen.route) {
                TasksScreen(navController)
            }
            composable(
                route = "${Screen.TaskDetail.route}?taskId={taskId}",
                arguments = listOf(
                    navArgument("taskId") {
//                        type = NavType.StringType
                        nullable = false
                    }
                )
            ) {
                val taskId = it.arguments?.getString("taskId")
                TaskDetailScreen(navController = navController, taskId = taskId!!)
            }
            composable(
                "${Screen.AddEditScreen.route}?taskId={taskId}",
                arguments = listOf(navArgument("taskId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })

            ) {
//                AddEditScreen(
//                    navController = navController,
//                    taskId = it.arguments?.getString("taskId")
//                )
            }
        })
}
